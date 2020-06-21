package com.xishi.basic.service;

import com.common.base.model.Resp;
import com.xishi.basic.entity.req.SmsReq;
import com.xishi.basic.entity.req.SmsValidateReq;

public interface ISmsService {

    /**
     * 发送短信
     */
    public Resp<Void> send(SmsReq smsReq);


    /**
     * 验证验证码
     */
    public Resp<Void> validate(SmsValidateReq smsValidateReq);

    //验证验证码
    public boolean checkCode(String phone,String code);
}
