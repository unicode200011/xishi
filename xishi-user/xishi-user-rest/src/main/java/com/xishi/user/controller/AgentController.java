package com.xishi.user.controller;


import com.cloud.webcore.annotation.NeedLogin;
import com.cloud.webcore.context.LoginContext;
import com.cloud.webcore.model.SessionUser;
import com.common.base.model.Req;
import com.common.base.model.Resp;
import com.xishi.user.entity.req.AgentApplyReq;
import com.xishi.user.entity.req.AgentInviteReq;
import com.xishi.user.entity.req.AgentQuery;
import com.xishi.user.entity.vo.*;
import com.xishi.user.model.pojo.User;
import com.xishi.user.service.IAgentService;
import com.xishi.user.service.ISmsService;
import com.xishi.user.service.IUserService;
import com.xishi.user.service.IUserWalletService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@Api(value = "家族相关接口", description = "家族相关接口")
@RequestMapping("/agent")
public class AgentController {

    @Autowired
    private IAgentService agentService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IUserWalletService userWalletService;

    @Autowired
    private ISmsService smsService;

    @PostMapping("/applyAgent")
    @ApiOperation("申请创建家族")
    @NeedLogin
    public Resp<AgentApplyVo> applyAgent(@RequestBody  Req<AgentApplyReq> req){
        SessionUser issuer = LoginContext.getLoginUser();
        Long userId = issuer.getUserId();
        User dbUser = userService.getById(userId);
        Resp<Void> checkResp = userService.checkUser(dbUser);
        if (!checkResp.isSuccess()) {
            return Resp.error(checkResp.getMsg());
        }
        AgentApplyReq data = req.getData();
        data.setUserId(userId);
        boolean b = smsService.checkCode(dbUser.getPhone(), data.getCode());
        if(!b){
            return Resp.error("验证码错误");
        }
        return agentService.applyAgent(data);
    }

    @PostMapping("/getMyAgentInfo")
    @ApiOperation("获取家族信息")
    @NeedLogin
    public Resp<AgentApplyVo> getMyAgentInfo(@RequestBody Req<Void> req){
        SessionUser issuer = LoginContext.getLoginUser();
        Long userId = issuer.getUserId();
        User dbUser = userService.getById(userId);
        Resp<Void> checkResp = userService.checkUser(dbUser);
        if (!checkResp.isSuccess()) {
            return Resp.error(checkResp.getMsg());
        }
        return agentService.getMyAgentInfo(userId);
    }


    @PostMapping("/getAgentShowerInfo")
    @ApiOperation("获取家族签约主播列表")
    @NeedLogin
    public Resp<List<AgentShowerVo>> getAgentShowerInfo(@RequestBody Req<AgentQuery> req){
        SessionUser issuer = LoginContext.getLoginUser();
        Long userId = issuer.getUserId();
        User dbUser = userService.getById(userId);
        Resp<Void> checkResp = userService.checkUser(dbUser);
        if (!checkResp.isSuccess()) {
            return Resp.error(checkResp.getMsg());
        }
        AgentQuery data = req.getData();
        data.setUserId(userId);
        return agentService.getAgentShowerInfo(data);
    }

    @PostMapping("/searchAgent")
    @ApiOperation("搜索家族")
    @NeedLogin
    public Resp<List<AgentSearchVo>> searchAgent(@RequestBody Req<AgentQuery> req){
        SessionUser issuer = LoginContext.getLoginUser();
        Long userId = issuer.getUserId();
        User dbUser = userService.getById(userId);
        Resp<Void> checkResp = userService.checkUser(dbUser);
        if (!checkResp.isSuccess()) {
            return Resp.error(checkResp.getMsg());
        }
        AgentQuery data = req.getData();
        data.setUserId(userId);
        return agentService.searchAgent(data);
    }

    @PostMapping("/searchUser")
    @ApiOperation("搜索用户")
    @NeedLogin
    public Resp<List<AgentSearchUserVo>> searchUser(@RequestBody Req<AgentQuery> req){
        SessionUser issuer = LoginContext.getLoginUser();
        Long userId = issuer.getUserId();
        User dbUser = userService.getById(userId);
        Resp<Void> checkResp = userService.checkUser(dbUser);
        if (!checkResp.isSuccess()) {
            return Resp.error(checkResp.getMsg());
        }
        AgentQuery data = req.getData();
        data.setUserId(userId);
        return agentService.searchUser(data);
    }

    @PostMapping("/inviteRecord")
    @ApiOperation("用户被邀请记录")
    @NeedLogin
    public Resp<List<AgentInviteAgentVo>> inviteRecord(@RequestBody Req<AgentQuery> req){
        SessionUser issuer = LoginContext.getLoginUser();
        Long userId = issuer.getUserId();
        User dbUser = userService.getById(userId);
        Resp<Void> checkResp = userService.checkUser(dbUser);
        if (!checkResp.isSuccess()) {
            return Resp.error(checkResp.getMsg());
        }
        AgentQuery data = req.getData();
        data.setUserId(userId);
        return agentService.inviteRecord(data);
    }

    @PostMapping("/agentInviteRecord")
    @ApiOperation("家族邀请记录")
    @NeedLogin
    public Resp<List<AgentInviteUserVo>> agentInviteRecord(@RequestBody Req<AgentQuery> req){
        SessionUser issuer = LoginContext.getLoginUser();
        Long userId = issuer.getUserId();
        User dbUser = userService.getById(userId);
        Resp<Void> checkResp = userService.checkUser(dbUser);
        if (!checkResp.isSuccess()) {
            return Resp.error(checkResp.getMsg());
        }
        AgentQuery data = req.getData();
        data.setUserId(userId);
        return agentService.agentInviteRecord(data);
    }

    @PostMapping("/agentInviteUser")
    @ApiOperation("家族邀请用户")
    @NeedLogin
    public Resp<Void> agentInviteUser(@RequestBody Req<AgentInviteReq> req){
        SessionUser issuer = LoginContext.getLoginUser();
        Long userId = issuer.getUserId();
        log.info("user:{}", userId);
        User dbUser = userService.getById(userId);
        Resp<Void> checkResp = userService.checkUser(dbUser);
        if (!checkResp.isSuccess()) {
            return Resp.error(checkResp.getMsg());
        }
        AgentInviteReq data = req.getData();
        data.setUserId(userId);
        return agentService.agentInviteUser(data);
    }

    @PostMapping("/userApply")
    @ApiOperation("用户申请加入家族")
    @NeedLogin
    public Resp<Void> userApply(@RequestBody Req<AgentInviteReq> req){
        SessionUser issuer = LoginContext.getLoginUser();
        Long userId = issuer.getUserId();
        User dbUser = userService.getById(userId);

        Resp<Void> checkResp = userService.checkUser(dbUser);

        if (!checkResp.isSuccess()) {
            return Resp.error(checkResp.getMsg());
        }

        if(userWalletService.isUserWallet(userId)){
            return Resp.error("请先把账户余额全部提现后再加入家族");
        }

        AgentInviteReq data = req.getData();
        data.setUserId(userId);
        return agentService.userApply(data);
    }

    @PostMapping("/shutdownApply")
    @ApiOperation("解除签约")
    @NeedLogin
    public Resp<Void> shutdownApply(@RequestBody Req<AgentInviteReq> req){
        SessionUser issuer = LoginContext.getLoginUser();
        Long userId = issuer.getUserId();
        User dbUser = userService.getById(userId);
        Resp<Void> checkResp = userService.checkUser(dbUser);
        if (!checkResp.isSuccess()) {
            return Resp.error(checkResp.getMsg());
        }

        if(userWalletService.isUserWallet(req.getData().getLinkUserId())){
            return Resp.error("请先通知主播把账户余额全部提现后再解约");
        }

        AgentInviteReq data = req.getData();
        data.setUserId(userId);
        return agentService.shutdownApply(data);
    }

    @PostMapping("/agreeInvite")
    @ApiOperation("用户同意/拒绝 邀请加入")
    @NeedLogin
    public Resp<Void> agreeInvite(@RequestBody Req<AgentInviteReq> req){
        SessionUser issuer = LoginContext.getLoginUser();
        Long userId = issuer.getUserId();
        User dbUser = userService.getById(userId);
        Resp<Void> checkResp = userService.checkUser(dbUser);
        if (!checkResp.isSuccess()) {
            return Resp.error(checkResp.getMsg());
        }

        if(req.getData().getJoinState() == 1 && userWalletService.isUserWallet(userId)){
            return Resp.error("请先把账户余额全部提现后再加入家族");
        }

        AgentInviteReq data = req.getData();
        data.setUserId(userId);
        return agentService.agreeInvite(data);
    }

}
