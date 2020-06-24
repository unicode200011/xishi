package com.xishi.user.controller.callback;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.internal.util.AlipaySignature;
import com.common.base.model.Resp;
import com.xishi.user.config.AliPayConfig;
import com.xishi.user.config.WxPayConfig;
import com.xishi.user.model.PayInfo;
import com.xishi.user.model.pojo.PayCallBackDataInfo;
import com.xishi.user.service.IPayAndTypeService;
import com.xishi.user.service.IRealnamePayService;
import com.xishi.user.util.pay.AliPay;
import com.xishi.user.util.pay.WxPay;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;

@RestController
@RequestMapping("/pay/callback")
@Api(value = "支付回调接口", description = "支付回调接口")
@Slf4j
public class PayCallbackController {

    @Autowired
    private AliPayConfig aliPayConfig;

    @Autowired
    private WxPayConfig wxPayConfig;

    @Autowired
    private IRealnamePayService realnamePayService;

    @Autowired
    private IPayAndTypeService payAndTypeService;

    @RequestMapping("/aliPayCallback")
    @ApiOperation("支付宝回调接口")
    public void aliPayCallback(HttpServletRequest request, HttpServletResponse response) {
        log.info("PayCallbackController aliPayCallback start............");
        Map<String, String> params = AliPay.parseAliPayNotifyParam(request);
        log.info("PayCallbackController aliPayCallback params={}", JSON.toJSONString(params));
        try {
            //验签
            boolean validate = AlipaySignature.rsaCheckV1(params, aliPayConfig.publicKey, aliPayConfig.charset, aliPayConfig.signType);
            if (!validate) {
                response.getWriter().println("failure");
                log.info("PayCallbackController aliPayCallback 支付宝回调验签失败");
                return;
            }
            String tradeStatus = params.get("trade_status");
            log.info("PayCallbackController aliPayCallback tradeStatus={}", tradeStatus);
            if (StringUtils.isBlank(tradeStatus) || !"TRADE_SUCCESS".equals(tradeStatus)) {
                response.getWriter().println("failure");
                return;
            }
            Map<String, String> passbackParams = JSONObject.parseObject(params.get("passback_params"), Map.class);
            String appId = passbackParams.get("appId");
            log.info("PayCallbackController aliPayCallback appId={}", appId);
            // 验证appId
            if (StringUtils.isBlank(appId) || !aliPayConfig.appId.equals(appId)) {
                response.getWriter().println("failure");
                log.info("PayCallbackController aliPayCallback 支付宝回调APPID不匹配");
                return;
            }

            Long uid = Long.valueOf(passbackParams.get("uid"));
            String orderNo = passbackParams.get("orderNum");
            BigDecimal payMoney = new BigDecimal(passbackParams.get("payMoney"));
            Integer payType = Integer.valueOf(passbackParams.get("payType"));
            //支付宝单号
            String tradeNo = params.get("trade_no");
            String buyerPayAmount = params.get("buyer_pay_amount");

            PayInfo payInfo = new PayInfo(uid, 0L, orderNo, payMoney, payType, tradeNo, buyerPayAmount);
            realnamePayService.payDeal(payInfo);
            response.getWriter().println("success");
        } catch (Exception e) {
            try {
                response.getWriter().println("failure");
            } catch (Exception ea) {
            }
            log.info("PayCallbackController aliPayCallback exception,支付宝回调异常", e);
        }
    }

    @RequestMapping("/bbPayCallback")
    @ApiOperation("币宝支付回调接口")
    public Resp bbPayCallBack(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> params = AliPay.parseAliPayNotifyParam(request);
        String s = JSONObject.toJSONString(params);
        PayCallBackDataInfo payCallBackDataInfo = JSON.parseObject(s, PayCallBackDataInfo.class);
        log.info("PayCallbackController 币宝支付回调接口 start............");
        log.info("PayCallbackController 币宝支付回调接口 params={}", params);
        log.info("PayCallbackController 币宝支付回调接口 payCallBackDataInfo={}", JSON.toJSONString(payCallBackDataInfo));
        return payAndTypeService.bbPayCallBack(payCallBackDataInfo);
    }

    /**
     * add 2020/6/20 by unicode
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/beePayCallback")
    @ApiOperation("bee支付回调接口")
    public Resp beePayCallback(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> params = AliPay.parseAliPayNotifyParam(request);
        String s = JSONObject.toJSONString(params);
        PayCallBackDataInfo payCallBackDataInfo = JSON.parseObject(s, PayCallBackDataInfo.class);
        log.info("PayCallbackController bee支付回调接口 start............");
        log.info("PayCallbackController bee支付回调接口 params={}", params);
        log.info("PayCallbackController bee支付回调接口 payCallBackDataInfo={}", JSON.toJSONString(payCallBackDataInfo));
        //如果接收到服务器点对点通讯时，在页面输出“OK”（没有双引号，OK两个字母大写）,否则会重复3次发送点对点通知.
        try {
            response.getWriter().println("OK");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return payAndTypeService.beePayCallBack(payCallBackDataInfo);
    }

    @RequestMapping("/wxPayCallback")
    @ApiOperation("微信支付回调接口")
    public void wxPayCallback(HttpServletRequest request, HttpServletResponse response) {
        log.info("PayCallbackController wxPayCallback start............");
        SortedMap<Object, Object> packageParams = WxPay.parseWxNotify(request);
        log.info("PayCallbackController wxPayCallback params={}", JSON.toJSONString(packageParams));
        try {
            boolean validate = WxPay.isWxPaySign(wxPayConfig.charset, packageParams);
            if (validate) {
                if ("SUCCESS".equals(String.valueOf(packageParams.get("result_code")))) {
                    //验证商户号
                    String mchId = String.valueOf(packageParams.get("mch_id"));
                    String wxTradeNo = String.valueOf(packageParams.get("transaction_id"));
                    if (!StringUtils.isNotBlank(mchId) && !wxPayConfig.mchId.equals(mchId)) {
                        Map<String, String> result = new HashMap<>();
                        result.put("return_code", "FAIL");
                        result.put("return_msg", "OK");
                        String xmlResult = WxPay.mapToXml(result);
                        response.getWriter().println(xmlResult);
                        log.info("【微信回调】微信回调商户号不匹配,回传商户号{}", mchId);
                        return;
                    }

                    // 回调返回;
                    Map<String, String> result = new HashMap<>();
                    result.put("return_code", "SUCCESS");
                    result.put("return_msg", "OK");
                    String xmlResult = WxPay.mapToXml(result);
                    response.getWriter().println(xmlResult);

                    //透传参数;
                    String attachStr = String.valueOf(packageParams.get("attach"));
                    Map<String, String> attachMap = (Map<String, String>) JSONObject.parse(attachStr);
                    String orderNo = attachMap.get("orderNum");
                    Long uid = Long.valueOf(attachMap.get("uid"));
                    Long exchangeId = Long.valueOf(attachMap.get("exchangeId"));
//                    BigDecimal payMoney = BigDecimal.valueOf(Double.valueOf(attachMap.get("payMoney")));
                    BigDecimal payMoney = BigDecimal.ZERO;
                    Integer payType = Integer.valueOf(attachMap.get("payType"));

                    PayInfo payInfo = new PayInfo(uid, exchangeId, orderNo, payMoney, payType, wxTradeNo, payMoney.toString());
                    realnamePayService.payDeal(payInfo);
//                    //充值
//                    if (payType == 0) {
//
//                    } else if (payType == 1) {
//                        //实名
//
//                    }
                }
            } else {
                log.info("【微信回调】微信支付回调验签失败");
            }
        } catch (Exception e) {
            try {
                response.getWriter().println("failure");
            } catch (Exception ea) {
            }
            log.info("PayCallbackController wxPayCallback exception,微信支付回调异常", e);
        }
    }

}
