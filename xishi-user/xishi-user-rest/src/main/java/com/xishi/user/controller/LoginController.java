package com.xishi.user.controller;

import com.cloud.webcore.annotation.NeedLogin;
import com.cloud.webcore.context.LoginContext;
import com.cloud.webcore.model.SessionUser;
import com.cloud.webcore.model.TokenInfo;
import com.cloud.webcore.service.RedisService;
import com.cloud.webcore.util.TokenUtil;
import com.common.base.model.Req;
import com.common.base.model.ReqHeader;
import com.common.base.model.Resp;
import com.common.base.util.MapUtil;
import com.xishi.user.entity.req.LoginReq;
import com.xishi.user.entity.vo.LoginResultVo;
import com.xishi.user.service.ILoginService;
import com.xishi.user.util.pay.AliPay;
import com.xishi.user.util.pay.WxPay;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

@RestController
@Slf4j
@Api(value = "登录接口", description = "登录接口")
public class LoginController {
    @Autowired
    private ILoginService loginService;

    RedisService redisService;

    @PostMapping("/login")
    @ApiOperation("登录")
    public Resp<LoginResultVo> login(@RequestBody @Valid Req<LoginReq> req, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Resp.error(bindingResult.getFieldError().getDefaultMessage());
        }
        ReqHeader header = req.getHeader();
        LoginReq loginReq = req.getData();
        String deviceTolen = (header == null || header.getDeviceToken() == null) ? null : header.getDeviceToken();
        loginReq.setDeviceToken(deviceTolen);
        log.info("LoginController login start,loginReq={}", loginReq);
        Resp<LoginResultVo> resp = loginService.login(loginReq);

        return resp;
    }

    @PostMapping("/logout")
    @ApiOperation("登出")
    @NeedLogin
    public Resp<Void> logout(@RequestBody Req<Void> req) {
        SessionUser userVo = LoginContext.getLoginUser();
        if (userVo == null) {
            return Resp.success();
        }
        Long userId = userVo.getUserId();
        loginService.logout(userId);
        return Resp.success();
    }

    @PostMapping("/userLoginNum")
    @ApiOperation("添加访问量")
    @NeedLogin
    private Resp<Void> userLoginNum(@RequestBody Req<Void> req){
        loginService.addVisitor();
        return new Resp<>();
    }

    @PostMapping("/checkLogin")
    @ApiOperation("检查是否登录")
    @NeedLogin
    public Resp<LoginResultVo> checkLogin(@RequestBody Req<Void> req) {
        SessionUser userVo = LoginContext.getLoginUser();
        if (userVo == null) {
            return new Resp<LoginResultVo>(401, "用户未登录");
        }
        Long userId = userVo.getUserId();
        boolean rt = loginService.checkLogin(userId);
        if (!rt) {
            return new Resp<LoginResultVo>(401, "用户未登录");
        }
        LoginResultVo resultVo = new LoginResultVo();
        resultVo.setUserId(userVo.getUserId());
        resultVo.setPhone(userVo.getPhone());
        Resp<LoginResultVo> resp = new Resp<LoginResultVo>(resultVo);
        return resp;
    }

    //根据token检查用户登录状态
    @PostMapping("/partnerCheckLogin")
    @ApiOperation("第三方检查是否登录")
    public Resp<LoginResultVo> partnerCheckLogin(@RequestBody Req<Void> req) {
        log.info("LoginController partnerCheckLogin start..........................");
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        TokenInfo tokenInfo = TokenUtil.tipTokenInfo(request);
        if (tokenInfo == null || !tokenInfo.hasCorrectToken()) {
            log.info("LoginController partnerCheckLogin fail,tokenInfo={}", tokenInfo);
            return new Resp<LoginResultVo>(401, "用户未登录");
        }
        if (tokenInfo.isExpired()) {
            log.info("LoginController partnerCheckLogin fail,tokenInfo={}", tokenInfo);
            return new Resp<LoginResultVo>(401, "用户登录过期");
        }
        Long userId = tokenInfo.getUserId();
        boolean rt = loginService.checkLoginByToken(userId, tokenInfo.getToken());
        if (!rt) {
            log.info("LoginController partnerCheckLogin checkLoginByToken fail,userId={}", userId);
            return new Resp<LoginResultVo>(401, "用户未登录");
        }
        LoginResultVo resultVo = new LoginResultVo();
        resultVo.setUserId(tokenInfo.getUserId());
        resultVo.setPhone(tokenInfo.getPhone());
        Resp<LoginResultVo> resp = new Resp<LoginResultVo>(resultVo);
        return resp;
    }

    @PostMapping("/aliPayLoginSign")
    @ApiOperation("获取支付宝登录签名")
    public Resp<Map> aliPayLoginAuth(@RequestBody Req<Void> req) {
        try {
            return Resp.successData(MapUtil.build().put("sign", AliPay.completeLogin()).over());
        } catch (Exception e) {
            log.error("获取支付宝登录签名失败", e);
            return Resp.error("获取支付宝登录签名失败");
        }
    }

}
