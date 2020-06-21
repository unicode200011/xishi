package com.xishi.basic.controller;

import com.common.base.model.Req;
import com.common.base.model.Resp;
import com.common.base.util.RequestUtil;
import com.xishi.basic.entity.req.SmsReq;
import com.xishi.basic.entity.req.SmsValidateReq;
import com.xishi.basic.service.ISmsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/sms")
@Slf4j
@Api(value="验证码接口",description="验证码接口")
public class SmsController {

    @Autowired
    private ISmsService smsService;

    @PostMapping("/sms")
    @ApiOperation("发送验证码")
    public Resp<Void> send(@RequestBody @Valid Req<SmsReq> req, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Resp.error(bindingResult.getFieldError().getDefaultMessage());
        }
        SmsReq smsReq = req.getData();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String ipAddress = RequestUtil.getIpAddress(request);
        log.info("SmsController send start,smsReq={},ipAddress={}",smsReq,ipAddress);
        //判断发送来源
        if (request.getHeader("User-Agent").startsWith("Mozilla")) {
            smsReq.setSourceType(1); //H5/浏览器
        } else {
            smsReq.setSourceType(0); //app
        }
        Resp<Void> resp= smsService.send(smsReq);
        return resp;
    }

    @PostMapping("/validate")
    @ApiOperation("验证验证码")
    public Resp<Void> validate(@RequestBody @Valid Req<SmsValidateReq> req, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Resp.error(bindingResult.getFieldError().getDefaultMessage());
        }
        SmsValidateReq smsValidateReq = req.getData();
        if(StringUtils.isBlank(smsValidateReq.getCode())) {
            return Resp.error("验证码为空");
        }
        log.info("SmsController validate start,smsValidateReq={}",smsValidateReq);
        Resp<Void> resp=  smsService.validate(smsValidateReq);
        return resp;
    }

}
