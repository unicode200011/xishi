package com.xishi.socket.feign;

import com.common.base.model.Req;
import com.common.base.model.Resp;
import com.xishi.user.entity.req.ChangeGradeReq;
import com.xishi.user.entity.req.SmsReq;
import com.xishi.user.entity.req.WatchRecordReq;
import com.xishi.user.entity.vo.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(name = "xishi-user")
public interface UserService {

    //验证交换密码，请求data为交换密码
    @PostMapping("/account/validatePayPwd")
    public Resp<Void> validatePayPwd(Req<String> req);

    //查询用户简单信息
    @PostMapping("/user/realname/queryRealnameInfo")
    public Resp<UserAuthVo> queryRealnameInfo(Req<Long> req);

    @PostMapping("/user/changeGrade")
    public Resp<Void> changeGrade(Req<ChangeGradeReq> req);

    @PostMapping("/user/userInviteInfo")
    public Resp<InvitationCodeVo> userInviteInfo(Req<Void> req);

    @PostMapping("/user/queryUserInfo")
    public Resp<UserInfoVo> queryUserInfo(Req<Long> req);

    @PostMapping("/sms/orderSms")
    public Resp<Void> sendSMS(Req<SmsReq> req);
    /**
     * 查验用户钱包
     * @return
     */
    @PostMapping("/user/checkUserWallet")
    Resp<UserWalletVo> checkUserWallet(Req<Long> req);

    @PostMapping("/user/replaceWord")
    Resp<String> replaceWord(Req<String> req);

    @PostMapping("/user/addWatchRecord")
    Resp<Void> addWatchRecord(Req<WatchRecordReq> req);

    @PostMapping("/user/getWatchRecord")
    Resp<WatchRecordReq> getWatchRecord(Req<WatchRecordReq> req);

    @PostMapping("/user/updateWatchRecord")
    Resp<Void> updateWatchRecord(Req<WatchRecordReq> req);

    @PostMapping("/dynamic/checkLevel")
    Resp<String> checkLevel(Req<Void> req);

    @PostMapping("/wallet/getLiveGiftRanking")
    Resp<List<LiveGiftRankingVo>> getLiveGiftRanking(Req<Long> req);

}
