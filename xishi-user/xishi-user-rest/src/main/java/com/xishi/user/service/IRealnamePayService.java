package com.xishi.user.service;

import com.common.base.model.Resp;
import com.xishi.user.entity.vo.WxPayVo;
import com.xishi.user.model.PayInfo;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface IRealnamePayService {

    //实名支付签名
    public Resp<String> realnamePaySign(WxPayVo data);

    //支付后的处理
    public boolean payDeal(PayInfo payInfo);

    Resp<Map> wxPaySign(WxPayVo wxPayVo, HttpServletRequest request);
}
