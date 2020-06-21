package com.xishi.user.controller;

import com.cloud.webcore.annotation.InnerInvoke;
import com.cloud.webcore.annotation.NeedLogin;
import com.cloud.webcore.context.LoginContext;
import com.cloud.webcore.model.SessionUser;
import com.common.base.model.Req;
import com.common.base.model.Resp;
import com.xishi.user.entity.req.BankUserReq;
import com.xishi.user.entity.req.ChargeRecordReq;
import com.xishi.user.entity.vo.*;
import com.xishi.user.model.pojo.ChargeRecord;
import com.xishi.user.service.IChargeRecordService;
import com.xishi.user.service.IUserWalletRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/wallet")
@Api(value = "用户钱包相关接口", description = "用户钱包相关接口")
public class WalletController {

    @Autowired
    private IChargeRecordService chargeRecordService;
    @Autowired
    private IUserWalletRecordService userWalletRecordService;

    @PostMapping("/getUserChargeRecord")
    @ApiOperation("用户充值列表")
    @NeedLogin
    public Resp<List<ChargeRecordVo>> getUserChargeRecord(@RequestBody Req<ChargeRecordReq> req){
        SessionUser sessionUser = LoginContext.getLoginUser();
        Long userId = sessionUser.getUserId();
        ChargeRecordReq data = req.getData();
        data.setUserId(userId);
       return chargeRecordService.getUserChargeRecord(data);
    }

    @PostMapping("/getUserWalletRecord")
    @ApiOperation("用户消费列表")
    @NeedLogin
    public Resp<List<UserWalletRecordVo>> getUserWalletRecord(@RequestBody Req<ChargeRecordReq> req){
        SessionUser sessionUser = LoginContext.getLoginUser();
        Long userId = sessionUser.getUserId();
        ChargeRecordReq data = req.getData();
        data.setUserId(userId);
        return userWalletRecordService.getUserWalletRecord(data);
    }
    @PostMapping("/getUserGiftRecord")
    @ApiOperation("用户收礼明细列表")
    @NeedLogin
    public Resp<List<UserWalletGiftRecordVo>> getUserGiftRecord(@RequestBody Req<ChargeRecordReq> req){
        SessionUser sessionUser = LoginContext.getLoginUser();
        Long userId = sessionUser.getUserId();
        ChargeRecordReq data = req.getData();
        data.setUserId(userId);
        return userWalletRecordService.getUserGiftRecord(data);
    }
    @PostMapping("/walletDetail")
    @ApiOperation("用户账户金额明细")
    @NeedLogin
    public Resp<MoneyDetailVo> walletDetail(@RequestBody Req<ChargeRecordReq> req){
        SessionUser sessionUser = LoginContext.getLoginUser();
        Long userId = sessionUser.getUserId();
        ChargeRecordReq data = req.getData();
        data.setUserId(userId);
        return userWalletRecordService.walletDetail(data);
    }

    @PostMapping("/getBankAccountType")
    @ApiOperation("获取账户分类列表")
    @NeedLogin
    public Resp<List<BankAccountTypeVo>> getBankAccountType(@RequestBody Req<Void> req){
        SessionUser sessionUser = LoginContext.getLoginUser();
        Long userId = sessionUser.getUserId();
        return userWalletRecordService.getBankAccountType(userId);
    }

    @PostMapping("/addBankAccount")
    @ApiOperation("添加用户银行账户")
    @NeedLogin
    public Resp<Void> addBankAccount(@RequestBody Req<BankUserReq> req){
        SessionUser sessionUser = LoginContext.getLoginUser();
        Long userId = sessionUser.getUserId();
        BankUserReq data = req.getData();
        data.setUserId(userId);
        return userWalletRecordService.addBankAccountType(data);
    }

    @PostMapping("/getUserBankAccountList")
    @ApiOperation("获取用户银行账户")
    @NeedLogin
    public Resp<List<BankUserVo>> getUserBankAccountList(@RequestBody Req<Void> req){
        SessionUser sessionUser = LoginContext.getLoginUser();
        Long userId = sessionUser.getUserId();
        return userWalletRecordService.getUserBankAccountList(userId);
    }

    @PostMapping("/deleteUserBankAccount")
    @ApiOperation("删除用户银行账户 只需传入账户id")
    @NeedLogin
    public Resp<Void> deleteUserBankAccount(@RequestBody Req<BankUserVo> req){
        SessionUser sessionUser = LoginContext.getLoginUser();
        Long userId = sessionUser.getUserId();
        BankUserVo data = req.getData();
        data.setUserId(userId);
        return userWalletRecordService.deleteUserBankAccount(data);
    }

    @PostMapping("/userWalletDetail")
    @ApiOperation("内部接口---用户账户金额")
    @InnerInvoke
    public Resp<MoneyDetailVo> userWalletDetail(@RequestBody Req<ChargeRecordReq> req){
        ChargeRecordReq data = req.getData();
        return userWalletRecordService.walletDetail(data);
    }

    @PostMapping("/getLiveGiftRanking")
    @ApiOperation("内部接口---顶部在线人数 排行榜")
    @InnerInvoke
    public Resp<List<LiveGiftRankingVo>> getLiveGiftRanking(@RequestBody Req<Long> req){
        return userWalletRecordService.getLiveGiftRanking(req.getData());
    }





}
