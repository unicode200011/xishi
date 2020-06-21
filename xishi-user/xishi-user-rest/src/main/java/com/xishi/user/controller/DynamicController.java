package com.xishi.user.controller;


import com.cloud.webcore.annotation.InnerInvoke;
import com.cloud.webcore.annotation.NeedLogin;
import com.cloud.webcore.context.LoginContext;
import com.cloud.webcore.model.SessionUser;
import com.common.base.model.Req;
import com.common.base.model.Resp;
import com.xishi.user.entity.req.*;
import com.xishi.user.entity.vo.DynamicCommentBackVo;
import com.xishi.user.entity.vo.DynamicCommentVo;
import com.xishi.user.entity.vo.DynamicVo;
import com.xishi.user.mq.MqDynamicSender;
import com.xishi.user.service.IDynamicCommentService;
import com.xishi.user.service.IDynamicService;
import com.xishi.user.service.IKeywordService;
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
@RequestMapping("/dynamic")
@Api(value = "动态相关接口",description = "动态相关接口")
public class DynamicController {

    @Autowired
    private IDynamicService dynamicService;

    @Autowired
    private IDynamicCommentService dynamicCommentService;
    @Autowired
    private IUserService userService;
    @Autowired
    private MqDynamicSender mqDynamicSender;

    @PostMapping("/sendDynamic")
    @ApiOperation("发布动态")
    @NeedLogin
    public Resp<Void> sendDynamic(@RequestBody Req<DynamicReq> req) {
        SessionUser issuer = LoginContext.getLoginUser();
        Long userId = issuer.getUserId();
        DynamicReq data = req.getData();
        data.setUserId(userId);

        String levelStr = userService.getCommentLevel(29);
        Integer level = Integer.valueOf(levelStr);//

        boolean dbUser = userService.checkLevel(userId,level);
        if(!dbUser){
            return Resp.error("您暂未开通主播权限或用户等级未满"+level+"级，无该操作权限");
        }
        mqDynamicSender.sendDynamicMessage(data);
        return Resp.success();
    }

    @PostMapping("/getDynamics")
    @ApiOperation("获取动态列表")
    @NeedLogin
    public Resp<List<DynamicVo>> getDynamics(@RequestBody Req<DynamicQuery> req) {
        SessionUser issuer = LoginContext.getLoginUser();
        Long userId = issuer.getUserId();

        DynamicQuery data = req.getData();
        if(data.getSourceType() == 1){ //看别人的
            if(data.getUserId() == null){
                return Resp.error("参数错误");
            }
        }else {
            String levelStr = userService.getCommentLevel(29);
            Integer level = Integer.valueOf(levelStr);//

            boolean dbUser = userService.checkLevel(userId,level);
            if(!dbUser){
                return Resp.error("您暂未开通主播权限或用户等级未满"+level+"级，无该操作权限");
            }
            data.setUserId(userId);
        }
        return dynamicService.getDynamics(data);
    }

    @PostMapping("/checkLevel")
    @ApiOperation("检查等级")
    @InnerInvoke
    public Resp<String> checkLevel(@RequestBody Req<Void> req) {
        String levelStr = userService.getCommentLevel(30);
        return Resp.successData(levelStr);
    }

    @PostMapping("/deleteDynamic")
    @ApiOperation("删除动态  只需要传id")
    @NeedLogin
    public Resp<Void> deleteDynamic(@RequestBody Req<DynamicReq> req) {
        SessionUser issuer = LoginContext.getLoginUser();
        Long userId = issuer.getUserId();
        DynamicReq data = req.getData();
        data.setUserId(userId);
        String levelStr = userService.getCommentLevel(29);
        Integer level = Integer.valueOf(levelStr);//

        boolean dbUser = userService.checkLevel(userId,level);
        if(!dbUser){
            return Resp.error("您暂未开通主播权限或用户等级未满"+level+"级，无该操作权限");
        }
        mqDynamicSender.deleteDynamicMessage(data);
        return Resp.success();
    }

    @PostMapping("/praiseDynamic")
    @ApiOperation("动态 点赞/取消点赞")
    @NeedLogin
    public Resp<Void> praiseDynamic(@RequestBody Req<DynamicReq> req) {
        SessionUser issuer = LoginContext.getLoginUser();
        Long userId = issuer.getUserId();
        DynamicReq data = req.getData();
        data.setUserId(userId);
        String levelStr = userService.getCommentLevel(29);
        Integer level = Integer.valueOf(levelStr);//

        boolean dbUser = userService.checkLevel(userId,level);
        if(!dbUser){
            return Resp.error("您暂未开通主播权限或用户等级未满"+level+"级，无该操作权限");
        }
        mqDynamicSender.sendDynamicPraiseMessage(data);
        return Resp.success();
    }

    @PostMapping("/submitDynamicComment")
    @ApiOperation("提交动态的评论")
    @NeedLogin
    public Resp<DynamicCommentVo> submitDynamicComment(@RequestBody Req<DynamicCommentReq> req) {
        SessionUser issuer = LoginContext.getLoginUser();
        Long userId = issuer.getUserId();
         DynamicCommentReq data = req.getData();
        data.setUserId(userId);
        String levelStr = userService.getCommentLevel(29);
        Integer level = Integer.valueOf(levelStr);//

        boolean dbUser = userService.checkLevel(userId,level);
        if(!dbUser){
            return Resp.error("您暂未开通主播权限或用户等级未满"+level+"级，无该操作权限");
        }
        return dynamicCommentService.submitDynamicComment(data);
    }

    @PostMapping("/submitDynamicCommentBack")
    @ApiOperation("提交动态评论的回复")
    @NeedLogin
    public Resp<DynamicCommentBackVo> submitDynamicCommentBack(@RequestBody Req<DynamicCommentBackReq> req) {
        SessionUser issuer = LoginContext.getLoginUser();
        Long userId = issuer.getUserId();
        DynamicCommentBackReq data = req.getData();
        data.setUserId(userId);
        String levelStr = userService.getCommentLevel(29);
        Integer level = Integer.valueOf(levelStr);//

        boolean dbUser = userService.checkLevel(userId,level);
        if(!dbUser){
            return Resp.error("您暂未开通主播权限或用户等级未满"+level+"级，无该操作权限");
        }
        return dynamicCommentService.submitDynamicCommentBack(data);
    }


    @PostMapping("/dynamicCommentList")
    @ApiOperation("获取动态的评论列表")
    @NeedLogin
    public Resp<List<DynamicCommentVo>> dynamicCommentList(@RequestBody Req<DynamicCommentQuery> req) {
        SessionUser issuer = LoginContext.getLoginUser();
        Long userId = issuer.getUserId();
        DynamicCommentQuery data = req.getData();
        data.setUserId(userId);
        String levelStr = userService.getCommentLevel(29);
        Integer level = Integer.valueOf(levelStr);//

        boolean dbUser = userService.checkLevel(userId,level);
        if(!dbUser){
            return Resp.error("您暂未开通主播权限或用户等级未满"+level+"级，无该操作权限");
        }
        return dynamicCommentService.DynamicCommentList(data);
    }

    @PostMapping("/dynamicCommentBackList")
    @ApiOperation("获取动态评论的回复列表")
    @NeedLogin
    public Resp<List<DynamicCommentBackVo>> dynamicCommentBackList(@RequestBody  Req<DynamicCommentQuery> req) {
        SessionUser issuer = LoginContext.getLoginUser();
        Long userId = issuer.getUserId();
        DynamicCommentQuery data = req.getData();
        data.setUserId(userId);
        String levelStr = userService.getCommentLevel(29);
        Integer level = Integer.valueOf(levelStr);//

        boolean dbUser = userService.checkLevel(userId,level);
        if(!dbUser){
            return Resp.error("您暂未开通主播权限或用户等级未满"+level+"级，无该操作权限");
        }
        return dynamicCommentService.dynamicCommentBackList(data);
    }



}
