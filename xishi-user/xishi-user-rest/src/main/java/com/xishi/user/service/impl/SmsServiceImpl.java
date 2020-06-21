package com.xishi.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.cloud.webcore.service.RedisService;
import com.common.base.model.Resp;
import com.common.base.util.*;
import com.common.base.util.HttpKit;
import com.component.sms.yuntongxun.SmsResult;
import com.component.sms.yuntongxun.SmsSender;
import com.xishi.user.config.SMSConfig;
import com.xishi.user.config.SystemConfig;
import com.xishi.user.dao.mapper.SmsRecordMapper;
import com.xishi.user.enums.SmsTemplateEnum;
import com.xishi.user.model.pojo.User;
import com.xishi.user.entity.req.SmsReq;
import com.xishi.user.entity.req.SmsValidateReq;
import com.xishi.user.entity.vo.SmsValidateResultVo;
import com.xishi.user.service.ISmsService;
import com.xishi.user.service.IUserService;
import com.xishi.user.util.*;
import com.xishi.user.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
@Slf4j
public class SmsServiceImpl implements ISmsService {

    @Autowired
    private RedisService redisService;

    @Autowired
    private SmsSender smsSender;

    @Autowired
    private IUserService userService;

    @Autowired
    private SystemConfig systemConfig;

    @Autowired
    private SMSConfig smsConfig;

    @Autowired
    private SmsRecordMapper smsRecordMapper;

    public Resp<Boolean> send(SmsReq smsReq) {
        String phone = smsReq.getPhone();
        Integer type = smsReq.getType()==null?0:smsReq.getType();
        if (!ToolUtil.phoneRegTwo(phone)) return Resp.error("手机号格式错误");
        User dbUser = this.userService.queryByPhone(phone);
        if (type == 0) {
            if (dbUser != null) return Resp.error("该手机号已注册");
        } else if (type == 1) {
            if (dbUser == null) return Resp.error("该手机号未注册");
        } else {
            return Resp.error("参数错误");
        }

        String mobileCode =this.getRandomNum(4);
        String url = smsConfig.getUrl();
        String content = DateUtils.getUnicodeString(smsConfig.getContent().replaceAll("CODE",mobileCode));
        String mercode = smsConfig.getMerCode();
        String timestamp = DateUtils.getTimeTro()+"";
        String telephone = "86-"+phone;
        String msgtype = "1";
        String paramStr ="mercode="+mercode +"&content="+content +"&telephone="+telephone +"&msgtype="+msgtype +"&timestamp="+timestamp;
        log.info("短信参数：paramStr=【{}】",paramStr);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String yyyymmdd = LocalDateTime.now(ZoneOffset.of("+8")).format(formatter);
        String B = MD5.GetMD5Code(smsConfig.getMerCode() + telephone + smsConfig.getKeyB() + yyyymmdd);
        String key = "qaz"+B+"qwertyu";
        log.info("短信参数：key=【{}】",key);
        Map requst = new HashMap();
        DESUtil desUtil = new DESUtil(smsConfig.getDesKey());
        try {
            requst.put("param", desUtil.encrypt(paramStr));
        } catch (Exception e) {
            e.printStackTrace();
        }
        requst.put("key",key);
        log.info("短信参数：url=【{}】",url);
        log.info("短信发送参数，requst=【{}】",JSONObject.toJSONString(requst));
        String respon = HttpKit.sendGet(url, requst);
        Map map = JSONObject.parseObject(respon, Map.class);
        log.info("短信返回参数，respon=【{}】",respon);
        Boolean success =(Boolean) map.get("Success");
        if (success) {
            String smsKey = systemConfig.getSmsPrefix().concat(phone);
            log.info("SmsServiceImpl send success,【短信发送】短信发送成功,手机号=【{}】,Code=【{}】,", phone, mobileCode);
            redisService.set(smsKey, mobileCode, systemConfig.getSmsExpireTime());
            return new Resp<>(dbUser!=null,200,"短信发送成功");
        }
        return new Resp<>(dbUser!=null,200,"短信发送失败");
    }

    public static void main(String[] args) {
        String mobileCode ="1234";
        String phone = "13281255998";
        String keyB = "d4rzo2MXgx";
        String desKey = "uWERwS2i";
        String url = "http://opoutox.gosafepp.com/api/nnxsqkjgs/sms/send";
        String content = DateUtils.getUnicodeString("您的西施直播短信验证码是1234,十分钟内有效,请勿告诉他人！");
        String mercode = "nnxsqkjgs";
        String timestamp = DateUtils.getTimeTro()+"";
        String telephone = "86-"+phone;
        String msgtype = "1";
        String paramStr ="mercode="+mercode +"&content="+content +"&telephone="+telephone +"&msgtype="+msgtype +"&timestamp="+timestamp;
        log.info("短信参数：paramStr=【{}】",paramStr);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String yyyymmdd = LocalDateTime.now(ZoneOffset.of("+8")).format(formatter);
        String keyStr = mercode + telephone + keyB + yyyymmdd;
        String B = MD5.GetMD5Code(keyStr);
        String key = "qaz"+B+"qwertyu";
        log.info("短信参数：keyStr=【{}】",keyStr);
        log.info("短信参数：key=【{}】",key);
        Map requst = new HashMap();
        DESUtil desUtil = new DESUtil(desKey);
        try {
            requst.put("param", desUtil.encrypt(paramStr));
        } catch (Exception e) {
            e.printStackTrace();
        }
        requst.put("key",key);
        log.info("短信参数：url=【{}】",url);
        log.info("短信发送参数，requst=【{}】",JSONObject.toJSONString(requst));
        String respon = HttpUtils.sendGet(url, requst);
        Map map = JSONObject.parseObject(respon, Map.class);
        log.info("短信返回参数，respon=【{}】",respon);
    }


    @Override
    public Resp<SmsValidateResultVo> validate(SmsValidateReq smsValidateReq) {
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
//            redisService.remove(smsKey);
        }
        User user = userService.queryByPhone(phone);
        boolean register = (user!=null);

        SmsValidateResultVo resultVo = new SmsValidateResultVo(register);
        Resp<SmsValidateResultVo> resp = new Resp<SmsValidateResultVo>(resultVo);
        return resp;
    }

    //验证验证码
    public boolean checkCode(String phone,String code) {
        if(StringUtils.isBlank(phone) || StringUtils.isBlank(code)) {
            return false;
        }
//        开发测试阶段，验证码6666直接通过
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

    @Override
    public Resp<Boolean> sendOrderSms(SmsReq smsReq) {
        String phone = smsReq.getPhone();
        if (!ToolUtil.phoneRegTwo(phone)) return Resp.error("手机号格式错误");
        Integer smsTemplateId=SmsTemplateEnum.ORDER_CODE.getTemplateId();
        SmsResult smsResult=smsSender.sendCode(smsTemplateId,phone,smsReq.getContent(),null);
        return Resp.success();
    }

    public String getRandomNum(int length) {
        String base = "0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();

        for(int i = 0; i < length; ++i) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }

        return sb.toString();
    }
}
