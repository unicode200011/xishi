package com.xishi.user.service;

import com.common.base.model.Resp;
import com.xishi.user.entity.req.SmsReq;
import com.xishi.user.entity.req.SmsValidateReq;
import com.xishi.user.entity.vo.SmsValidateResultVo;

public interface ISmsService {

    /**
     * 发送短信
     */
    public Resp<Boolean> send(SmsReq smsReq);


    /**
     * 验证验证码
     */
    public Resp<SmsValidateResultVo> validate(SmsValidateReq smsValidateReq);

    //验证验证码
    public boolean checkCode(String phone,String code);

    Resp<Boolean> sendOrderSms(SmsReq smsReq);
}
