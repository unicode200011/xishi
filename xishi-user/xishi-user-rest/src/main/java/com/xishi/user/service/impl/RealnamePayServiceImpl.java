package com.xishi.user.service.impl;

import com.cloud.webcore.service.RedisService;
import com.common.base.model.Resp;
import com.common.base.util.MakeOrderNum;
import com.common.base.util.MapUtil;
import com.xishi.user.config.AliPayConfig;
import com.xishi.user.config.Config;
import com.xishi.user.config.WxPayConfig;
import com.xishi.user.entity.req.GetTaskReq;
import com.xishi.user.entity.vo.WxPayVo;
import com.xishi.user.model.PayInfo;
import com.xishi.user.model.pojo.PayRecord;
import com.xishi.user.model.pojo.UserAuthInfo;
import com.xishi.user.service.IPayRecordService;
import com.xishi.user.service.IRealnamePayService;
import com.xishi.user.service.IUserService;
import com.xishi.user.util.pay.AliPay;
import com.xishi.user.util.pay.WxPay;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;

@Service
@Slf4j
public class RealnamePayServiceImpl implements IRealnamePayService {

    @Autowired
    private IUserService userService;

    @Autowired
    private AliPayConfig aliPayConfig;

    @Autowired
    private WxPayConfig wxPayConfig;

    @Autowired
    private IPayRecordService payRecordService;

    @Autowired
    private RedisService redisService;

    @Value("${pay.env}")
    private Boolean payEnv ;

    //实名认证支付签名
    public Resp<String> realnamePaySign(WxPayVo data) {
        try {
            Long userId = data.getUserId();
            String orderNum = MakeOrderNum.makeOrderNum("RN");
            BigDecimal money =data.getAmount();

            if(!payEnv) {
               money = BigDecimal.valueOf(Double.valueOf("0.01"));

            }else if(money.compareTo(BigDecimal.valueOf(1)) < 0){
               return Resp.error("最低充值金额不能低于1元");
            }

            Map<String, String> publicParams = new HashMap<String, String>();
            publicParams.put("appId", aliPayConfig.appId);
            publicParams.put("orderNum", orderNum);
            publicParams.put("uid", ""+userId);
            publicParams.put("payMoney", money.toString());
            publicParams.put("exchangeId",data.getExchangeId().toString());
            String title = "西施短视频支付宝支付";
            String sign = AliPay.aliPay(new AliPay.AliPayParam(money, title, orderNum, publicParams, AliPay.PayType.QUICK_MSECURITY_PAY));
            //创建支付记录
            createPayRecord(userId, title, money, AliPay.NOTIFY_URL, orderNum, 0);
            Resp<String> resp = new Resp<String>(sign);
            return resp;
        } catch (Exception e) {
            log.error("RealnamePayServiceImpl realnamePay 生成支付宝签名错误", e);
            return Resp.error("系统错误,请稍后再试");
        }
    }

    //创建支付记录
    public void createPayRecord(Long userId, String title, BigDecimal money, String notifyUrl, String orderNum, int type) {
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
        payRecordService.save(payRecord);
    }

    //支付后的处理
    public boolean payDeal(PayInfo payInfo) {
        Long userId = payInfo.getUserId();
        String orderNo = payInfo.getOrderNo();
        log.info("RealnamePayServiceImpl payDeal start,userId={},orderNo={},payInfo={}",userId,orderNo,payInfo);
        int state=1;//1--回调且验签通过
        //支付记录
        payRecordService.updatePayRecord(userId, orderNo, payInfo.getTradeNo(),state, "");
        return true;
    }

    @Override
    public Resp<Map> wxPaySign(WxPayVo wxPayVo, HttpServletRequest request) {
        try {
            //0 充值 1 实名
            int payType = wxPayVo.getPayType();
            Map<String, String> publicParams = new HashMap<>();
            String body = "";
            String orderNum ="";
            BigDecimal money = wxPayVo.getAmount();

            if (payType == 0) {
                if(payEnv) {
                    if (money.compareTo(BigDecimal.valueOf(1)) < 0) {
                        return Resp.error("最低充值金额不能低于1元");
                    }
                }else {
                    money = BigDecimal.valueOf(0.01);
                }

                body = "西施短视频微信支付";
                orderNum = MakeOrderNum.makeOrderNum("CZ");
                log.info("【微信充值】用户ID:【{}】,充值金额:【{}】", wxPayVo.getUserId(), money);

            } else if (payType == 1) {

                body = "西施短视频实名认证";
                if(payEnv) {
                    money = BigDecimal.valueOf(1.5);
                }else {
                    money = BigDecimal.valueOf(0.01);
                }
                orderNum = MakeOrderNum.makeOrderNum("RN");
                log.info("【微信-实名认证】用户ID:【{}】", wxPayVo.getUserId());
            }
            publicParams.put("appId", wxPayConfig.appId);
            publicParams.put("orderNum", orderNum);
            publicParams.put("uid", wxPayVo.getUserId().toString());
            publicParams.put("payMoney", money.toString());
            publicParams.put("exchangeId",wxPayVo.getExchangeId().toString());
            SortedMap<Object, Object> resultParameter = WxPay.wxPay(new WxPay.WxPayParam(money, body, orderNum, publicParams, request, WxPay.PayType.APP));
            if (resultParameter.get("sign").toString().equals(WxPay.FAIL)) {
                return Resp.error(resultParameter.get("msg").toString());
            }
            //创建支付记录
            createPayRecord(wxPayVo.getUserId(), body, money, WxPay.NOTIFY_URL, orderNum, 1);
            return Resp.successData(MapUtil.build().put("sign", resultParameter).over());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("微信充值】生成微信签名错误", e);
            return Resp.error("系统错误,请稍后再试");
        }
    }
}
