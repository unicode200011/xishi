package com.xishi.basic.service.impl;

import com.cloud.webcore.service.RedisService;
import com.common.base.model.Resp;
import com.common.base.util.ToolUtil;
import com.component.sms.yuntongxun.SmsResult;
import com.component.sms.yuntongxun.SmsSender;
import com.xishi.basic.config.SystemConfig;
import com.xishi.basic.constant.SystemConstants;
import com.xishi.basic.dao.mapper.SmsRecordMapper;
import com.xishi.basic.enums.SmsTemplateEnum;
import com.xishi.basic.model.pojo.SmsRecord;
import com.xishi.basic.entity.req.SmsReq;
import com.xishi.basic.entity.req.SmsValidateReq;
import com.xishi.basic.service.ISmsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class SmsServiceImpl implements ISmsService {

    @Autowired
    private RedisService redisService;

    @Autowired
    private SmsSender smsSender;

    @Autowired
    private SmsRecordMapper smsRecordMapper;

    @Autowired
    private SystemConfig systemConfig;

    public static final String SMS_CODE_EFFECT_STR = "10分钟";

    public Resp<Void> send(SmsReq smsReq) {
        String phone = smsReq.getPhone();
        if (!ToolUtil.phoneRegTwo(phone)) return Resp.error("手机号格式错误");
        String mobileCode =ToolUtil.getRandomNum(4);
        String [] otherArgs = new String [] {SMS_CODE_EFFECT_STR};
        Integer smsTemplateId=SmsTemplateEnum.VERIFY_CODE.getTemplateId();
        SmsResult smsResult=smsSender.sendCode(smsTemplateId,phone,mobileCode,otherArgs);
        Integer state = smsResult.getState();
        String reason =smsResult.getReason();

        SmsRecord record = new SmsRecord();
        record.setPhone(phone);
        record.setCode(mobileCode);
        record.setReason(reason);
        record.setCreateTime(new Date());
        record.setState(state);
        smsRecordMapper.insert(record);

        if (smsResult.isSuccess()) {
            log.info("SmsServiceImpl send success,【短信发送】短信发送成功,手机号=【{}】,Code=【{}】,", phone, mobileCode);
            redisService.toCache(systemConfig.getSmsPrefix().concat(phone), mobileCode, systemConfig.getSmsExpireTime());
            return Resp.success("短信发送成功");
        }
        return Resp.success("短信发送失败,请稍后重试");
    }

    @Override
    public Resp<Void> validate(SmsValidateReq smsValidateReq) {
        String phone = smsValidateReq.getPhone();
        String code = smsValidateReq.getCode();
        Integer type = smsValidateReq.getType()==null?0:smsValidateReq.getType();

        //开发测试阶段，验证码6666直接通过
        if(!"6666".equalsIgnoreCase(code)) {
            String smsKey = systemConfig.getSmsPrefix().concat(phone);
            Object redisCode = redisService.get(smsKey);
            if (null == redisCode) {
                return Resp.error("验证码已过期,请重新获取");
            }
            if (!code.equals(String.valueOf(redisCode))) {
                return Resp.error("验证码错误");
            }
            redisService.remove(smsKey);
        }

        //1 找回密码 2 验证码登录 0 公共验证
        if (type == 1) {
            redisService.set(SystemConstants.SMS_FIND_FIND_SET_PASS_KEY.concat(phone), 1, systemConfig.getSmsExpireTime());
        } else if (type == 2) {
            redisService.set(SystemConstants.SMS_LOGIN_KEY.concat(phone), 1, systemConfig.getSmsExpireTime());
        } else {
            redisService.set(SystemConstants.SMS_PUBLIC_KEY.concat(phone), 1, systemConfig.getSmsExpireTime());
        }
        return Resp.success();
    }

    //验证验证码
    public boolean checkCode(String phone,String code) {
        if(StringUtils.isBlank(phone) || StringUtils.isBlank(code)) {
            return false;
        }
        //开发测试阶段，验证码6666直接通过
        if("6666".equalsIgnoreCase(code)) {
            return true;
        }

        String smsKey =systemConfig.getSmsPrefix().concat(phone);
        Object redisCode = redisService.get(smsKey);
        if (null == redisCode) {
            return false;
        }
        if (!code.equals(String.valueOf(redisCode))) {
            return false;
        }
        redisService.remove(smsKey);
        return true;
    }
}
