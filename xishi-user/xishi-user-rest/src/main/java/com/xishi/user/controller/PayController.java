package com.xishi.user.controller;


import com.cloud.webcore.annotation.InnerInvoke;
import com.cloud.webcore.annotation.NeedLogin;
import com.cloud.webcore.context.LoginContext;
import com.cloud.webcore.model.SessionUser;
import com.common.base.model.Req;
import com.common.base.model.Resp;
import com.common.base.util.TransUtil;
import com.xishi.user.entity.req.BBPayReq;
import com.xishi.user.entity.req.BBPaySubmitReq;
import com.xishi.user.entity.vo.*;
import com.xishi.user.enums.UserAuthStateEnum;
import com.xishi.user.model.PayResponInfo;
import com.xishi.user.model.pojo.ChargeList;
import com.xishi.user.model.pojo.PayAndType;
import com.xishi.user.model.pojo.User;
import com.xishi.user.model.pojo.UserAuthInfo;
import com.xishi.user.service.IChargeListService;
import com.xishi.user.service.IPayAndTypeService;
import com.xishi.user.service.IRealnamePayService;
import com.xishi.user.service.IUserService;
import com.xishi.user.util.AliIdentifyAuthUtil;
import com.xishi.user.util.HttpUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pay")
@Api(value = "支付相关接口", description = "支付相关接口")
@Slf4j
public class PayController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IRealnamePayService realnamePayService;

    @Autowired
    private IChargeListService chargeListService;

    @Autowired
    private IPayAndTypeService payAndTypeService;

    @PostMapping("/aliPaySign")
    @ApiOperation("支付宝支付签名")
    @NeedLogin
    public Resp<String> realnamePaySign(@RequestBody Req<WxPayVo> req) {
        SessionUser userVo = LoginContext.getLoginUser();
        WxPayVo data = req.getData();
        Long userId = userVo.getUserId();
        data.setUserId(userId);
        Resp<String> resp = realnamePayService.realnamePaySign(data);
        return resp;
    }

    @PostMapping("/wxPaySign")
    @ApiOperation("微信支付签名")
    @NeedLogin
    public Resp<Map> realnamePaySign(@RequestBody Req<WxPayVo> req, HttpServletRequest request) {
        SessionUser userVo = LoginContext.getLoginUser();
        Long userId = userVo.getUserId();
        WxPayVo data = req.getData();
        data.setUserId(userId);
        Resp<Map> resp = realnamePayService.wxPaySign(data, request);
        return resp;
    }

    @PostMapping("/chargeList")
    @ApiOperation("充值页数据")
    @NeedLogin
    public Resp<ChargeVo> chargeList(@RequestBody Req<Void> req) {
        SessionUser userVo = LoginContext.getLoginUser();
        Long userId = userVo.getUserId();
        Resp<ChargeVo> resp = chargeListService.chargeList(userId);
        return resp;
    }

    @PostMapping("/submitChargeInfo")
    @ApiOperation("提交充值---币宝")
    @NeedLogin
    public Resp<Map> submitChargeInfo(@RequestBody Req<BBPayVo> req) {
        SessionUser userVo = LoginContext.getLoginUser();
        Long userId = userVo.getUserId();
        BBPayVo data = req.getData();
        data.setUserId(userId);
        return chargeListService.submitChargeInfo(data);
    }

    @PostMapping("/getPayWayList")
    @ApiOperation("获取支付方式列表")
    public Resp<List<BBPayWayVo>> getPayWayList(@RequestBody Req<BBPayReq> req) {
        BBPayReq data = req.getData();
        return payAndTypeService.getPayWayList(data);
    }

    @PostMapping("/submitPayWay")
    @ApiOperation("提交支付")
    @NeedLogin
    public Resp<PayResponInfo> submitPayWay(@RequestBody Req<BBPaySubmitReq> req, HttpServletRequest request) {
        SessionUser userVo = LoginContext.getLoginUser();
        Long userId = userVo.getUserId();
        BBPaySubmitReq data = req.getData();
        data.setUserId(userId);
        String ipAddress = HttpUtils.getIpAddress(request);
        data.setIp(ipAddress);
        return chargeListService.submitPayWay(data);
    }

    /**
     * Bee支付
     * add 2020/6/20 by unicode
     *
     * @param req
     * @param request
     * @return
     */
    @PostMapping("/submitBeePayWay")
    @ApiOperation("提交支付")
    @NeedLogin
    public String submitBeePayWay(@RequestBody Req<BBPaySubmitReq> req, HttpServletRequest request) {
        SessionUser userVo = LoginContext.getLoginUser();
        Long userId = userVo.getUserId();
        BBPaySubmitReq data = req.getData();
        data.setUserId(userId);
        String ipAddress = HttpUtils.getIpAddress(request);
        data.setIp(ipAddress);
        return chargeListService.submitBeePayWay(data);
    }

}
