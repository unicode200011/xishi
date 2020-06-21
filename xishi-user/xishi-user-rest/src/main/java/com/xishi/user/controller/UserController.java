package com.xishi.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cloud.webcore.annotation.InnerInvoke;
import com.cloud.webcore.annotation.NeedLogin;
import com.cloud.webcore.context.LoginContext;
import com.cloud.webcore.model.SessionUser;
import com.cloud.webcore.service.RedisService;
import com.common.base.model.Req;
import com.common.base.model.Resp;
import com.common.base.util.StrKit;
import com.common.base.util.TransUtil;
import com.component.cos.CosHelper;
import com.component.cos.CosResult;
import com.xishi.user.entity.constant.SystemConstants;
import com.xishi.user.entity.req.*;
import com.xishi.user.model.pojo.InvitationCode;
import com.xishi.user.model.pojo.User;
import com.xishi.user.entity.vo.*;
import com.xishi.user.model.pojo.UserWallet;
import com.xishi.user.model.pojo.WatchRecord;
import com.xishi.user.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/user")
@Api(value = "用户接口", description = "用户接口")
@Slf4j
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IAccountService accountService;

    @Autowired
    private IInvitationCodeService invitationCodeService;

    @Autowired
    private CosHelper cosHelper;
    @Autowired
    private IUserWalletService userWalletService;
    @Autowired
    private IUserWalletRecordService userWalletRecordService;

    @Autowired
    private ISmsService smsService;

    @Autowired
    private RedisService redisService;



    @PostMapping("/userInfo")
    @ApiOperation("用户个人资料")
    @NeedLogin
    public Resp<UserInfoVo> userInfo(@RequestBody Req<Void> req) {
        SessionUser userVo = LoginContext.getLoginUser();
        Long userId = userVo.getUserId();
        UserInfoVo userInfo = userService.userInfo(userId);
        Resp<UserInfoVo> resp = new Resp<UserInfoVo>(userInfo);
        return resp;
    }

    @PostMapping("/otherUserInfo")
    @ApiOperation("查看其他人的用户个人资料")
    @NeedLogin
    public Resp<UserInfoVo> otherUserInfo(@RequestBody Req<UserReq> req) {
        SessionUser userVo = LoginContext.getLoginUser();
        Long userId = userVo.getUserId();
        UserReq data = req.getData();
        data.setUserId(userId);
        UserInfoVo userInfo = userService.otherUserInfo(data);
        if(userInfo == null){
            return Resp.error("参数错误");
        }
        Resp<UserInfoVo> resp = new Resp<UserInfoVo>(userInfo);
        return resp;
    }



    @PostMapping("/uploadAvatar")
    @ApiOperation("上传个人头像")
    @NeedLogin
    public Resp<UploadVo> uploadAvatar(@RequestParam(value = "file") MultipartFile file,
                                       @RequestParam(value = "type", required = false, defaultValue = "default") String fileType) {
        SessionUser sessionUser = LoginContext.getLoginUser();
        Long userId = sessionUser.getUserId();
        String fileName = file.getOriginalFilename().split("\\.")[0];
        log.info("UserController uploadAvatar start, fileName={},userId={}", fileName, sessionUser.getUserId());
        String toPath = SystemConstants.AVATAR_PATH + "/" + (userId / 1000);
        CosResult cosResult = cosHelper.uploadResource(toPath, file);
        if (!cosResult.isSuccess()) {
            return Resp.error("上传失败,请稍后重试");
        }
        UploadVo uploadVo = new UploadVo();
        uploadVo.setImagePath(cosResult.getOrglUrl());
        uploadVo.setSavePath(cosResult.getFullPath());
        uploadVo.setFileName(fileName);
        Resp<UploadVo> resp = new Resp<UploadVo>(uploadVo);
        return resp;
    }

    @PostMapping("/updateUserInfo")
    @ApiOperation("用户编辑资料")
    @NeedLogin
    public Resp<Void> updateUserInfo(@RequestBody Req<UserVo> req) {
        SessionUser issuer = LoginContext.getLoginUser();
        Long userId = issuer.getUserId();
        User dbUser = userService.getById(userId);
        Resp<Void> checkResp = userService.checkUser(dbUser);
        if (!checkResp.isSuccess()) {
            return checkResp;
        }
        UserVo userVo = req.getData();
        User user = TransUtil.transEntity(userVo, User.class);
        user.setId(userId);
        Resp<Void> resp = userService.updateUserInfo(user);
        return resp;
    }
    @PostMapping("/applyUserAuthInfo")
    @ApiOperation("实名认证申请")
    @NeedLogin
    public Resp<Void> applyUserAuthInfo(@RequestBody Req<AuthRequestVo> req) {
        SessionUser issuer = LoginContext.getLoginUser();
        Long userId = issuer.getUserId();
        User dbUser = userService.getById(userId);
        Resp<Void> checkResp = userService.checkUser(dbUser);
        if (!checkResp.isSuccess()) {
            return checkResp;
        }
        AuthRequestVo data = req.getData();
        data.setPhone(dbUser.getPhone());
        data.setUserId(dbUser.getId());
        Resp<Void> resp = userService.applyUserAuthInfo(data);
        return resp;
    }

    @PostMapping("/getUserAuthInfo")
    @ApiOperation("获取实名认证申请信息")
    @NeedLogin
    public Resp<UserAuthVo> getUserAuthInfo(@RequestBody Req<Void> req) {
        SessionUser issuer = LoginContext.getLoginUser();
        Long userId = issuer.getUserId();
        User dbUser = userService.getById(userId);
        Resp<Void> checkResp = userService.checkUser(dbUser);
        if (!checkResp.isSuccess()) {
            return Resp.error(checkResp.getMsg());
        }
        Resp<UserAuthVo> resp = userService.getUserAuthInfo(userId);
        return resp;
    }

    @PostMapping("/updateUserAuthInfo")
    @ApiOperation("更新实名认证申请信息")
    @NeedLogin
    public Resp<UserAuthVo> updateUserAuthInfo(@RequestBody Req<AuthRequestVo> req) {
        SessionUser issuer = LoginContext.getLoginUser();
        Long userId = issuer.getUserId();
        User dbUser = userService.getById(userId);
        Resp<Void> checkResp = userService.checkUser(dbUser);
        if (!checkResp.isSuccess()) {
            return Resp.error(checkResp.getMsg());
        }
        AuthRequestVo data = req.getData();
        data.setUserId(userId);
        data.setPhone(dbUser.getPhone());
        Resp<UserAuthVo> resp = userService.updateUserAuthInfo(data);
        return resp;
    }

    @PostMapping("/userInviteInfo")
    @ApiOperation("用户邀请码信息")
    @NeedLogin
    public Resp<InvitationCodeVo> userInviteInfo(@RequestBody Req<Void> req) {
        SessionUser issuer = LoginContext.getLoginUser();
        Long userId = issuer.getUserId();
        User user = userService.getById(userId);
        Resp<Void> checkResp = userService.checkUser(user);
        if (!checkResp.isSuccess()) {
            return new Resp<InvitationCodeVo>(checkResp.getCode(), checkResp.getMsg());
        }

        InvitationCode invitationCode = invitationCodeService.queryByUser(userId);
        InvitationCodeVo codeVo = TransUtil.transEntity(invitationCode, InvitationCodeVo.class);
        if (codeVo == null) {
            codeVo = new InvitationCodeVo();
            codeVo.setUserId(userId);
        }
        codeVo.setName(user.getName());
        codeVo.setAvatar(user.getAvatar());
        String inviteRegUrl = invitationCodeService.getInviteRegUrl();
        String inviteUrl = inviteRegUrl + codeVo.getCode();
        codeVo.setShareUrl(inviteUrl);
        Resp<InvitationCodeVo> resp = new Resp<InvitationCodeVo>(codeVo);
        return resp;
    }

    @PostMapping("/genInviteQrCode")
    @ApiOperation("生成用户邀请码二维码,app不用调")
    public Resp<Void> genInviteQrCode(@RequestBody Req<Long> req) {
        Long userId = req.getData();
        log.info("UserController genInviteQrCode start,userId ={}", userId);
        invitationCodeService.genInviteQrCode(userId);
        return Resp.success();
    }

    @PostMapping("/queryUserSimple")
    @ApiOperation("查询用户简单信息--内部接口,app或其他系统别调")
    @InnerInvoke
    public Resp<UserSimpleVo> queryUserSimple(@RequestBody Req<Long> req) {
        Long userId = req.getData();
        if (userId == null || userId <= 0) {
            return Resp.error("参数错误");
        }
        User user = userService.getById(userId);
        UserSimpleVo userSimpleVo = TransUtil.transEntity(user, UserSimpleVo.class);
        Resp<UserSimpleVo> resp = new Resp<UserSimpleVo>(userSimpleVo);
        return resp;
    }

    @ApiOperation("查询用户展示信息--内部接口,app或其他系统别调")
    @PostMapping("/queryUserInfo")
    @InnerInvoke
    public Resp<UserInfoVo> queryUserInfo(@RequestBody Req<Long> req) {
        Long userId = req.getData();
        UserInfoVo userInfo = userService.userInfo(userId);
        Resp<UserInfoVo> resp = new Resp<UserInfoVo>(userInfo);
        return resp;
    }

    @ApiOperation("查询用户钱包信息--内部接口,app或其他系统别调")
    @PostMapping("/checkUserWallet")
    @InnerInvoke
    public Resp<UserWalletVo> checkUserWallet(@RequestBody Req<Long> req) {
        Long userId = req.getData();
        UserWallet userWallet = userWalletService.getOne(new QueryWrapper<UserWallet>().eq("user_id",userId));
        UserWalletVo userWalletVo = TransUtil.transEntity(userWallet, UserWalletVo.class);
        Resp<UserWalletVo> resp = new Resp<UserWalletVo>(userWalletVo);
        return resp;
    }

    @ApiOperation("查询直播间贡献榜--内部接口,app或其他系统别调")
    @PostMapping("/getLiveGiftList")
    @InnerInvoke
    public Resp<List<LiveGiftListVo>> getLiveGiftList(@RequestBody Req<LiveGiftListQuery> req) {
        LiveGiftListQuery query = req.getData();
        return userWalletRecordService.getLiveGiftList(query);
    }

    @ApiOperation("查询是否关注--内部接口,app或其他系统别调")
    @PostMapping("/checkAttention")
    @InnerInvoke
    public Resp<Integer> checkAttention(@RequestBody Req<UserAttentionQuery> req) {
        UserAttentionQuery query = req.getData();
        return  userService.checkAttention(query);
    }


    @ApiOperation("搜索用户--内部接口,app或其他系统别调")
    @PostMapping("/searchUser")
    @InnerInvoke
    public Resp<List<UserInfoVo>> searchUser(@RequestBody Req<UserSearchQuery> req) {
        UserSearchQuery query = req.getData();
        List<UserInfoVo> list = userService.queryUserList(query);
        if (list == null) {
            list = Collections.EMPTY_LIST;
        }
        Resp<List<UserInfoVo>> resp = new Resp<List<UserInfoVo>>(list);
        return resp;
    }


    @Autowired
    private IKeywordService keywordService;
    @PostMapping("/replaceWord")
    @ApiOperation("屏蔽关键字  ---内部调用")
    @InnerInvoke
    public Resp<String> replaceWord(@RequestBody  Req<String> req) {
        String data = req.getData();
        String s = keywordService.replaceWord(data);
        return Resp.successData(s);
    }

    @Autowired
    private IWatchRecordService watchRecordService;

    @PostMapping("/addWatchRecord")
    @ApiOperation("添加观看记录  ---内部调用")
    @InnerInvoke
    public Resp<Void> addWatchRecord(@RequestBody  Req<WatchRecordReq> req) {
        log.info("添加观看记录[{}]",req.getData());
        WatchRecordReq data = req.getData();
        if(data.getLiveRecordId() != null){
            WatchRecord dbwatchRecord = watchRecordService.getOne(new QueryWrapper<WatchRecord>().eq("user_id", data.getUserId()).eq("live_record_id", data.getLiveRecordId()));
            if(dbwatchRecord == null){
                WatchRecord watchRecord = TransUtil.transEntity(data, WatchRecord.class);
                watchRecord.setLiveRecordId(data.getLiveRecordId());
                watchRecordService.save(watchRecord);
            }else {
                dbwatchRecord.setState(0);
                dbwatchRecord.setCreateTime(new Date());
                watchRecordService.updateById(dbwatchRecord);
            }
        }
        return Resp.success();
    }

    @PostMapping("/getWatchRecord")
    @ApiOperation("获取观看记录  ---内部调用")
    @InnerInvoke
    public Resp<WatchRecordReq> getWatchRecord(@RequestBody  Req<WatchRecordReq> req) {
        WatchRecordReq data = req.getData();
        WatchRecord watchRecord = watchRecordService.getOne(new QueryWrapper<WatchRecord>().eq("user_id", data.getUserId()).eq("live_record_id", data.getLiveRecordId()));
        if(watchRecord == null){
            return Resp.error("该记录不存在");
        }
        WatchRecordReq watchRecordReq = TransUtil.transEntity(watchRecord, WatchRecordReq.class);
        return Resp.successData(watchRecordReq);
    }

    @PostMapping("/updateWatchRecord")
    @ApiOperation("更新观看记录  ---内部调用")
    @InnerInvoke
    public Resp<Void> updateWatchRecord(@RequestBody  Req<WatchRecordReq> req) {
        WatchRecordReq data = req.getData();
        WatchRecord watchRecord = watchRecordService.getOne(new QueryWrapper<WatchRecord>().eq("user_id", data.getUserId()).eq("live_record_id", data.getLiveRecordId()));
        if(watchRecord == null){
            return Resp.error("该记录不存在");
        }
        Date endTime = watchRecord.getEndTime();
        if(endTime != null){
            Date now = new Date();
            Long time = (now.getTime() - endTime.getTime()) / 1000 / 60 ;
            watchRecord.setTime(time);
        }

        watchRecord.setEndTime(new Date());
        watchRecord.setState(1);
        watchRecordService.updateById(watchRecord);
        return Resp.success();
    }


    @PostMapping("/getUserGradeDetail")
    @ApiOperation("获取用户等级信息")
    @NeedLogin
    public Resp<UserGradeDetailVo> getUserGradeDetail(@RequestBody Req<Void> req) {
        SessionUser issuer = LoginContext.getLoginUser();
        Long userId = issuer.getUserId();
        User dbUser = userService.getById(userId);
        Resp<Void> checkResp = userService.checkUser(dbUser);
        if (!checkResp.isSuccess()) {
            return Resp.error(checkResp.getMsg());
        }

        return userService.getUserGradeDetail(userId);
    }


    @PostMapping("/getUserLiveGradeDetail")
    @ApiOperation("获取用户直播等级信息")
    @NeedLogin
    public Resp<UserGradeDetailVo> getUserLiveGradeDetail(@RequestBody Req<Void> req) {
        SessionUser issuer = LoginContext.getLoginUser();
        Long userId = issuer.getUserId();
        User dbUser = userService.getById(userId);
        Resp<Void> checkResp = userService.checkUser(dbUser);
        if (!checkResp.isSuccess()) {
            return Resp.error(checkResp.getMsg());
        }
        return userService.getUserLiveGradeDetail(userId);
    }

    @PostMapping("/isPayPwd")
    @ApiOperation("判断是否有支付密码 0否 1是")
    @NeedLogin
    public Resp<Integer> isPayPwd(@RequestBody Req<Void> req){
        SessionUser issuer = LoginContext.getLoginUser();
        Long userId = issuer.getUserId();

        return userService.isPayPwd(userId);
    }








}