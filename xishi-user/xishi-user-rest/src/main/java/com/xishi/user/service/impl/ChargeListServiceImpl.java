package com.xishi.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.common.base.model.Resp;
import com.common.base.util.MakeOrderNum;
import com.common.base.util.TransUtil;
import com.xishi.user.config.BBPayConfig;
import com.xishi.user.entity.req.BBLoginReq;
import com.xishi.user.entity.req.BBPaySubmitReq;
import com.xishi.user.entity.vo.BBPayVo;
import com.xishi.user.entity.vo.ChargeListVo;
import com.xishi.user.entity.vo.ChargeVo;
import com.xishi.user.model.PayResponInfo;
import com.xishi.user.model.pojo.*;
import com.xishi.user.dao.mapper.ChargeListMapper;
import com.xishi.user.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xishi.user.util.DateUtils;
import com.xishi.user.util.HttpUtils;
import com.xishi.user.util.JzSignUtil;
import com.xishi.user.util.MD5;
import com.xishi.user.util.pay.AliPay;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 充值列表 服务实现类
 * </p>
 *
 * @author LX
 * @since 2019-11-06
 */
@Slf4j
@Service
public class ChargeListServiceImpl extends ServiceImpl<ChargeListMapper, ChargeList> implements IChargeListService {

    @Autowired
    IUserWalletService userWalletService;
    @Autowired
    IUserService userService;
    @Autowired
    BBPayConfig bbPayConfig;
    @Autowired
    private IPayRecordService payRecordService;
    @Autowired
    private IPayService payService;
    @Autowired
    private IPayTypeService payTypeService;
    @Autowired
    private IPayAndTypeService payAndTypeService;

    @Override
    public Resp<ChargeVo> chargeList(Long userId) {
        Resp<Void> voidResp = userService.checkUser(userService.getById(userId));
        if (!voidResp.isSuccess()) {
            return Resp.error(voidResp.getMsg());
        }

        List<ChargeList> list = this.list();
        List<ChargeListVo> chargeListVos = TransUtil.transList(list, ChargeListVo.class);
        ChargeVo chargeVo = new ChargeVo();
        chargeVo.setChargeListVos(chargeListVos);

        String value = baseMapper.findCommon();
        chargeVo.setRate(Integer.valueOf(value));

        BigDecimal amount = userWalletService.findByUserId(userId);
        chargeVo.setAmount(amount);
        Resp<ChargeVo> resp = new Resp<ChargeVo>(chargeVo);
        return resp;
    }

    @Override
    @Transactional
    public Resp<Map> submitChargeInfo(BBPayVo data) {
        Resp<Map> loginUrlFromBB = null;
        try {
            Long userId = data.getUserId();
            User user = userService.getById(userId);
            String orderNum = MakeOrderNum.makeOrderNum("RN");
            BigDecimal money = data.getAmount();

            if (!bbPayConfig.getEnv().equals("true")) {
                money = BigDecimal.valueOf(Double.valueOf("0.01"));
            } else if (money.compareTo(BigDecimal.valueOf(1)) < 0) {
                return Resp.error("最低充值金额不能低于1元");
            }
            Integer chargeId = data.getChargeId();
            ChargeList chargeList = this.getById(chargeId);
            BigDecimal gbMoney = BigDecimal.ZERO;
            BigDecimal giftMoney = BigDecimal.ZERO;
            if (chargeList != null) {
                gbMoney = BigDecimal.valueOf(chargeList.getXishiNum());
                BigDecimal rate = chargeList.getRate();
                if (rate.compareTo(BigDecimal.ZERO) > 0) {
                    giftMoney = gbMoney.multiply(rate);
                }
            } else {
                gbMoney = money.multiply(BigDecimal.valueOf(100));
            }

            String title = "西施直播支付";
            //创建支付记录
            createPayRecord(userId, title, money, "", orderNum, data.getPayType(), gbMoney, giftMoney);
            //获取数字货币登录地址
            BBLoginReq bbLoginReq = new BBLoginReq();
            bbLoginReq.setAmount(money);
            bbLoginReq.setType(1);//买币
            bbLoginReq.setOrderNum(orderNum);
            bbLoginReq.setPayType(data.getPayType());
            bbLoginReq.setXishiNo(user.getXishiNum());
            loginUrlFromBB = userService.getLoginUrlFromBB(bbLoginReq);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return loginUrlFromBB;
    }

    @Override
    public Resp<PayResponInfo> submitPayWay(BBPaySubmitReq data) {
        Long userId = data.getUserId();
        User user = userService.getById(userId);
        int payWayId = data.getPayWayId();
        PayAndType payAndType = payAndTypeService.getById(payWayId);
        if (payAndType == null) {
            Resp.error("参数错误");
        }
        String orderNum = MakeOrderNum.makeOrderNum("RN");
        BigDecimal money = data.getAmount();

        Integer chargeId = data.getChargeId();
        ChargeList chargeList = this.getById(chargeId);
        BigDecimal gbMoney = BigDecimal.ZERO;
        BigDecimal giftMoney = BigDecimal.ZERO;
        if (chargeList != null) {
            gbMoney = BigDecimal.valueOf(chargeList.getXishiNum());
            BigDecimal rate = chargeList.getRate();
//            if(rate.compareTo(BigDecimal.ZERO) > 0){
//                giftMoney = gbMoney.multiply(rate);
//            }
        } else {
            String rate = baseMapper.findCommon();
            gbMoney = money.multiply(new BigDecimal(rate));
        }

        String title = "西施直播支付";
        //创建支付记录
        createPayRecord(userId, title, money, "", orderNum, payAndType.getWay(), gbMoney, giftMoney);

        // 如果是payID为3 调用 bee支付方式
        return this.getPayResponse(payAndType, data.getIp(), orderNum, money, user.getXishiNum());
    }


    /**
     * Bee支付
     *
     * @param data
     * @return
     */
    @Override
    public Resp<PayResponInfo> submitBeePayWay(BBPaySubmitReq data) {
        Long userId = data.getUserId();
        User user = userService.getById(userId);
        int payWayId = data.getPayWayId();
        PayAndType payAndType = payAndTypeService.getById(payWayId);
        if (payAndType == null) {
            Resp.error("参数错误");
        }
        String orderNum = MakeOrderNum.makeOrderNum("RN");
        BigDecimal money = data.getAmount();

        Integer chargeId = data.getChargeId();
        ChargeList chargeList = this.getById(chargeId);
        BigDecimal gbMoney = BigDecimal.ZERO;
        BigDecimal giftMoney = BigDecimal.ZERO;
        if (chargeList != null) {
            gbMoney = BigDecimal.valueOf(chargeList.getXishiNum());
            BigDecimal rate = chargeList.getRate();
//            if(rate.compareTo(BigDecimal.ZERO) > 0){
//                giftMoney = gbMoney.multiply(rate);
//            }
        } else {
            String rate = baseMapper.findCommon();
            gbMoney = money.multiply(new BigDecimal(rate));
        }

        String title = "西施直播Bee支付";
        //创建支付记录
        createPayRecord(userId, title, money, "", orderNum, payAndType.getWay(), gbMoney, giftMoney);

        return this.getPayBeeResponse(payAndType, data.getIp(), orderNum, money, user.getXishiNum());
    }

    //创建支付记录
    public void createPayRecord(Long userId, String title, BigDecimal money, String notifyUrl, String orderNum, int type, BigDecimal gbMoney, BigDecimal giftMoney) {
        Date now = new Date();
        //支付记录
        PayRecord payRecord = new PayRecord();
        payRecord.setUserId(userId);
        payRecord.setTitle(title);
        payRecord.setMoney(money);
        payRecord.setNotifyUrl(notifyUrl);
        payRecord.setOrderNo(orderNum);
        payRecord.setState(0);
        //0 支付宝  1 微信
        payRecord.setType(type);
        payRecord.setCreateTime(now);
        payRecord.setUpdateTime(now);
        payRecord.setGiftMoney(giftMoney);
        payRecord.setGbMoney(gbMoney);
        payRecordService.save(payRecord);
    }

    public Resp<PayResponInfo> getPayResponse(PayAndType payAndType, String ip, String orderNum, BigDecimal amount, String xishiNum) {


        Integer typeId = payAndType.getTypeId();
        Integer payId = payAndType.getPayId();
        Pay pay = payService.getById(payId);
        PayType payType = payTypeService.getById(typeId);


        Map param = new HashMap();
        param.put("notifyUrl", pay.getNotifyUrl());
        param.put("jExtra", payAndType.getId() + "");
        param.put("currency", "CNY");
        param.put("amount", amount.setScale(2, BigDecimal.ROUND_DOWN) + "");
        param.put("payWay", payType.getType() + "");
        param.put("orderType", "1");
        param.put("jOrderId", orderNum);
        param.put("jUserIp", ip);
        param.put("jUserId", xishiNum);
        param.put("signatureVersion", "1");
        param.put("signatureMethod", pay.getEncodeType() + "");
        param.put("merchantId", pay.getMerchantId());
        param.put("timestamp", System.currentTimeMillis() + "");

        String sign = JzSignUtil.createSign(param, pay.getApiKey());
        System.out.println("sign:【" + sign + "】");
        log.info("sign:【{}】", sign);

        param.put("signature", sign);

        String paramStr = DateUtils.getHttpGetParam(false, "", param);
        System.out.println("paramStr:【" + paramStr + "】");

        String respon = HttpUtils.sendPost(pay.getCreateMusterUrl(), paramStr);
        System.out.println("响应respon：" + respon);
        PayResponInfo payResponInfo = JSONObject.parseObject(respon, PayResponInfo.class);


        log.info("payResponInfo【{}】", payResponInfo);
        log.info("code【{}】", payResponInfo.getCode());
        log.info("Message【{}】", payResponInfo.getMessage());

        if (payResponInfo.getCode() != 0) {
            return Resp.error(payResponInfo.getMessage());
        }
        return Resp.successData(payResponInfo);
    }


    /**
     * bee 支付api拼装调用
     *
     * @param payAndType
     * @param ip
     * @param orderNum
     * @param amount
     * @param xishiNum
     * @return
     */
    public Resp<PayResponInfo> getPayBeeResponse(PayAndType payAndType, String ip, String orderNum, BigDecimal amount, String xishiNum) {

        Integer typeId = payAndType.getTypeId();
        Integer payId = payAndType.getPayId();
        Pay pay = payService.getById(payId);
        PayType payType = payTypeService.getById(typeId);

        //下单参数
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
        //提交时间
        String pay_applydate = df.format(new Date());
        //银行编码 api提供
        String pay_bankcode = "903";
        //同步跳转页面,暂时为空
        String pay_callbackurl = "";
        //交易名称
        String pay_productname = "西施小额支付";
        // 组合下单参数并MD5加密
        StringBuffer stringSignTemp = new StringBuffer();
        stringSignTemp.append("pay_amount=").append(amount.setScale(2, BigDecimal.ROUND_DOWN) + "").append("&");
        stringSignTemp.append("pay_applydate=").append(pay_applydate).append("&");
        stringSignTemp.append("pay_bankcode=").append(pay_bankcode).append("&");
        stringSignTemp.append("pay_callbackurl=").append(pay_callbackurl).append("&");
        stringSignTemp.append("pay_memberid=").append(pay.getMerchantId()).append("&");
        stringSignTemp.append("pay_notifyurl=").append(pay.getNotifyUrl()).append("&");
        stringSignTemp.append("pay_orderid=").append(orderNum).append("&");
        stringSignTemp.append("key=").append(pay.getApiKey());

        String pay_md5sign = MD5.GetMD5Code(String.valueOf(stringSignTemp)).toUpperCase();

        //完整下单参数

        StringBuffer postParam = new StringBuffer();
        postParam.append("pay_amount=").append(amount.setScale(2, BigDecimal.ROUND_DOWN) + "").append("&");
        postParam.append("pay_applydate=").append(pay_applydate).append("&");
        postParam.append("pay_bankcode=").append(pay_bankcode).append("&");
        postParam.append("pay_callbackurl=").append(pay_callbackurl).append("&");
        postParam.append("pay_memberid=").append(pay.getMerchantId()).append("&");
        postParam.append("pay_notifyurl=").append(pay.getNotifyUrl()).append("&");
        postParam.append("pay_orderid=").append(orderNum).append("&");
        postParam.append("pay_md5sign=").append(pay_md5sign).append("&");
        postParam.append("pay_productname=").append(pay_productname);
        System.out.println("完整参数:" + postParam);

        String respon = HttpUtils.sendPost(pay.getCreateMusterUrl(), String.valueOf(postParam));

        PayResponInfo payResponInfo = new PayResponInfo();
        payResponInfo.setCode(200);
        payResponInfo.setMessage(respon);
        System.out.println("响应respon：" + payResponInfo.getMessage());
        return Resp.successData(payResponInfo);

    }


    public static void main(String[] args) {

        //下单参数
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
        String pay_memberid = "200697206";
        String pay_orderid = "test11111";
        String pay_applydate = df.format(new Date());
        String pay_bankcode = "903";
        String pay_notifyurl = "https://www.baidu.com/";
        String pay_callbackurl = "";
        String pay_amount = "10";
        String pay_productname = "xishi_test_pay";
        String masterUrl = "http://p.sfpay.vip/Pay_Index.html";
        String key = "gokc6fruzluful7w22ze1fewemh36yg9";
        // 组合下单参数并MD5加密
        StringBuffer stringSignTemp = new StringBuffer();
        stringSignTemp.append("pay_amount=").append(pay_amount).append("&");
        stringSignTemp.append("pay_applydate=").append(pay_applydate).append("&");
        stringSignTemp.append("pay_bankcode=").append(pay_bankcode).append("&");
        stringSignTemp.append("pay_callbackurl=").append(pay_callbackurl).append("&");
        stringSignTemp.append("pay_memberid=").append(pay_memberid).append("&");
        stringSignTemp.append("pay_notifyurl=").append(pay_notifyurl).append("&");
        stringSignTemp.append("pay_orderid=").append(pay_orderid).append("&");
        stringSignTemp.append("key=").append(key);

        System.out.println("加密前:" + stringSignTemp);

        String pay_md5sign = MD5.GetMD5Code(String.valueOf(stringSignTemp)).toUpperCase();

        System.out.println("加密后:" + pay_md5sign);

        //完整下单参数
        StringBuffer postParam = new StringBuffer();
        postParam.append("pay_amount=").append(pay_amount).append("&");
        postParam.append("pay_applydate=").append(pay_applydate).append("&");
        postParam.append("pay_bankcode=").append(pay_bankcode).append("&");
        postParam.append("pay_callbackurl=").append(pay_callbackurl).append("&");
        postParam.append("pay_memberid=").append(pay_memberid).append("&");
        postParam.append("pay_notifyurl=").append(pay_notifyurl).append("&");
        postParam.append("pay_orderid=").append(pay_orderid).append("&");
        postParam.append("pay_md5sign=").append(pay_md5sign).append("&");
        postParam.append("pay_productname=").append(pay_productname);
        System.out.println("完整参数:" + postParam);


        String respon = HttpUtils.sendPost(masterUrl, String.valueOf(postParam));
        System.out.println("响应respon：" + respon);
        PayResponInfo payResponInfo = new PayResponInfo();
    }


}
