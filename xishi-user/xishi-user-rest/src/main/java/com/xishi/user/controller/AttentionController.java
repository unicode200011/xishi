package com.xishi.user.controller;


import com.cloud.webcore.annotation.NeedLogin;
import com.cloud.webcore.context.LoginContext;
import com.cloud.webcore.model.SessionUser;
import com.common.base.model.Req;
import com.common.base.model.Resp;
import com.xishi.user.entity.req.*;
import com.xishi.user.entity.vo.DynamicCommentBackVo;
import com.xishi.user.entity.vo.DynamicCommentVo;
import com.xishi.user.entity.vo.DynamicVo;
import com.xishi.user.entity.vo.FansUserVo;
import com.xishi.user.mq.MqDynamicSender;
import com.xishi.user.service.IAttentionService;
import com.xishi.user.service.IDynamicCommentService;
import com.xishi.user.service.IDynamicService;
import com.xishi.user.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 动态列表 前端控制器
 * </p>
 *
 * @author LX
 * @since 2019-11-14
 */
@RestController
@RequestMapping("/attention")
@Api(value = "关注/粉丝相关接口",description = "关注/粉丝相关接口")
public class AttentionController {

    @Autowired
    private IDynamicService dynamicService;

    @Autowired
    private IDynamicCommentService dynamicCommentService;
    @Autowired
    private IUserService userService;
    @Autowired
    private MqDynamicSender mqDynamicSender;
    @Autowired
    private IAttentionService attentionService;

    @PostMapping("/getFansList")
    @ApiOperation("获取粉丝/关注列表")
    @NeedLogin
    public Resp<List<FansUserVo>> getFansList(@RequestBody Req<AttentionQuery> req) {
        SessionUser issuer = LoginContext.getLoginUser();
        Long userId = issuer.getUserId();
        AttentionQuery data = req.getData();
        data.setUserId(userId);
        return attentionService.getFansList(data);
    }

    @PostMapping("/getAttentionDynamicList")
    @ApiOperation("获取关注的人的动态列表 只需传分页参数，其他的不用传")
    @NeedLogin
    public Resp<List<DynamicVo>> getAttentionDynamicList(@RequestBody Req<DynamicQuery> req) {
        SessionUser issuer = LoginContext.getLoginUser();
        Long userId = issuer.getUserId();
        DynamicQuery data = req.getData();
        data.setUserId(userId);
        return attentionService.getAttentionDynamicList(data);
    }

    @PostMapping("/submitAttention")
    @ApiOperation("添加关注/取消关注")
    @NeedLogin
    public Resp<Void> submitAttention(@RequestBody Req<AttentionReq> req) {
        SessionUser issuer = LoginContext.getLoginUser();
        Long userId = issuer.getUserId();
        AttentionReq data = req.getData();
        data.setUserId(userId);
        return attentionService.submitAttention(data);
    }



}
