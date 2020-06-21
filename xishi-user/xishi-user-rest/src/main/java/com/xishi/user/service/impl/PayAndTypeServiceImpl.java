package com.xishi.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.common.base.model.Resp;
import com.common.base.util.TransUtil;
import com.xishi.user.entity.req.BBPayReq;
import com.xishi.user.entity.vo.BBPayWayVo;
import com.xishi.user.model.pojo.*;
import com.xishi.user.dao.mapper.PayAndTypeMapper;
import com.xishi.user.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 支付方式和支付类型关联表 服务实现类
 * </p>
 *
 * @author LX
 * @since 2020-02-14
 */
@Service
public class PayAndTypeServiceImpl extends ServiceImpl<PayAndTypeMapper, PayAndType> implements IPayAndTypeService {
    @Autowired
    private IPayService payService;
    @Autowired
    private IPayRecordService payRecordService;
    @Autowired
    private IUserWalletService userWalletService;
    @Autowired
    private IUserWalletRecordService userWalletRecordService;
    @Autowired
    private IChargeRecordService chargeRecordService;

    @Override
    public Resp<List<BBPayWayVo>> getPayWayList(BBPayReq data) {
        List<PayAndType> way = this.list(new QueryWrapper<PayAndType>().eq("way", data.getPayType()));
        List<BBPayWayVo> bbPayWayVos = TransUtil.transList(way, BBPayWayVo.class);
        return Resp.successData(bbPayWayVos);
    }

    @Override
    @Transactional
    public Resp bbPayCallBack(PayCallBackDataInfo payCallBackDataInfo) {
        //查询订单信息
        String jExtra = payCallBackDataInfo.getJExtra();
        PayAndType payAndType = this.getById(jExtra);
        Integer payId = payAndType.getPayId();
        Pay pay = payService.getById(payId);
        Integer status = payCallBackDataInfo.getStatus();
        if (status == 2 || status == 3) { //已支付 已完成
            String jOrderId = payCallBackDataInfo.getJOrderId();
            String orderId = payCallBackDataInfo.getOrderId();
            PayRecord payRecord = payRecordService.getOne(new QueryWrapper<PayRecord>().eq("order_no", jOrderId));
            if (payRecord != null) {
                payRecord.setState(1);
                payRecord.setTradeNo(orderId);
                payRecordService.updateById(payRecord);
                //新增用户钱包金额
                Long userId = payRecord.getUserId();
                BigDecimal add = payRecord.getGbMoney().add(payRecord.getGiftMoney());
                userWalletService.addByUserId(userId, add);
                //新增用户钱包记录
                UserWalletRecord userWalletRecord = new UserWalletRecord();
                userWalletRecord.setType(0);
                userWalletRecord.setUserId(userId);
                userWalletRecord.setAmount(add);
                userWalletRecord.setUseType(0);
                userWalletRecord.setRemark("充值");
                BigDecimal byUserId = userWalletService.findByUserId(userId);
                userWalletRecord.setWalletAmount(byUserId);
                userWalletRecordService.save(userWalletRecord);
                //添加充值记录
                ChargeRecord chargeRecord = new ChargeRecord();
                chargeRecord.setRmbAmount(BigDecimal.valueOf(payCallBackDataInfo.getAmount()));
                chargeRecord.setSource(payAndType.getWay() == 0 ? 1 : 0);
                chargeRecord.setUserId(userId);
                chargeRecord.setXishiAmount(add);
                chargeRecord.setWalletAmount(byUserId);
                chargeRecord.setOrderNum(orderId);
                chargeRecord.setXishiOrderNum(jOrderId);
                chargeRecordService.save(chargeRecord);
            }
        }

        return new Resp(0, "回调成功");
    }

    /**
     * add 2020/6/20 by unicode
     * bee支付接口回调
     *
     * @param payCallBackDataInfo
     * @return
     */
    @Override
    @Transactional
    public Resp beePayCallBack(PayCallBackDataInfo payCallBackDataInfo) {
        //获取bee返回交易状态 00为支付成功
        String returncode = payCallBackDataInfo.getReturncode();
        // 支付成功
        if (returncode.equals(00)) {
            //流水号 支付接口生成
            String Transaction_id = payCallBackDataInfo.getTransaction_id();
            //订单号 xishi系统生成
            String orderId = payCallBackDataInfo.getOrderId();
            //根据 xishi系统生成的订单号 获取 交易记录 eb_pay_record
            PayRecord payRecord = payRecordService.getOne(new QueryWrapper<PayRecord>().eq("order_no", orderId));
            if (payRecord != null) {
                payRecord.setState(1);
                payRecord.setTradeNo(Transaction_id);
                //更新 交易记录
                payRecordService.updateById(payRecord);
                Long userId = payRecord.getUserId();
                //充值虚拟币+赠送虚拟币
                BigDecimal add = payRecord.getGbMoney().add(payRecord.getGiftMoney());
                //更新西施币数量 eb_user_wallet   gb_moeny
                userWalletService.addByUserId(userId, add);
                //新增用户钱包记录
                UserWalletRecord userWalletRecord = new UserWalletRecord();
                userWalletRecord.setType(0);
                userWalletRecord.setUserId(userId);
                userWalletRecord.setAmount(add);
                userWalletRecord.setUseType(0);
                userWalletRecord.setRemark("充值");
                BigDecimal byUserId = userWalletService.findByUserId(userId);
                userWalletRecord.setWalletAmount(byUserId);
                userWalletRecordService.save(userWalletRecord);

                //添加充值记录
                ChargeRecord chargeRecord = new ChargeRecord();
                //充值人民币金额
                chargeRecord.setRmbAmount(BigDecimal.valueOf(payCallBackDataInfo.getAmount()));
                //充值途径  1= 支付宝
                chargeRecord.setSource(1);
                //充值用户
                chargeRecord.setUserId(userId);
                //西施币数量
                chargeRecord.setXishiAmount(add);
                //充值后钱包余额
                chargeRecord.setWalletAmount(byUserId);
                //表结构里面order_num 代表 第三方流水号
                chargeRecord.setOrderNum(Transaction_id);
                //表结构里面xishi_order_num 代表 xishi生成的订单号
                chargeRecord.setXishiOrderNum(orderId);
                chargeRecordService.save(chargeRecord);
            }
        }

        return new Resp(0, "回调成功");
    }
}
