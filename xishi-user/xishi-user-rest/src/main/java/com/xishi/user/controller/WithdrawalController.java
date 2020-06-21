package com.xishi.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cloud.webcore.annotation.NeedLogin;
import com.cloud.webcore.context.LoginContext;
import com.cloud.webcore.model.SessionUser;
import com.common.base.model.Req;
import com.common.base.model.Resp;
import com.xishi.user.entity.req.WithdrawalRecordReq;
import com.xishi.user.entity.req.WithdrawalSubmitReq;
import com.xishi.user.entity.vo.UserVo;
import com.xishi.user.entity.vo.WithdrawalVo;
import com.xishi.user.model.pojo.Withdrawal;
import com.xishi.user.model.pojo.WithdrawalSet;
import com.xishi.user.service.IUserService;
import com.xishi.user.service.IWithdrawalService;
import com.xishi.user.service.IWithdrawalSetService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/withdrawal")
@Api(value = "用户提现相关接口", description = "用户提现相关接口")
public class WithdrawalController {

    @Autowired
    private IWithdrawalService withdrawalService;
    @Autowired
    private IWithdrawalSetService withdrawalSetService;
    @Autowired
    private IUserService userService;

    @PostMapping("/getWithdrawalList")
    @ApiOperation("提现数据列表")
    public Resp<List<BigDecimal>> getWithdrawalList(){
        List<WithdrawalSet> list = withdrawalSetService.list();
        List<BigDecimal> collect = list.stream().map(WithdrawalSet::getGbMoney).collect(Collectors.toList());
        return Resp.successData(collect);
    }

    @PostMapping("/withdrawalSubmit")
    @ApiOperation("提交提现申请")
    @NeedLogin
    public Resp<Void> withdrawalSubmit(@RequestBody Req<WithdrawalSubmitReq> req){
        SessionUser issuer = LoginContext.getLoginUser();
        Long userId = issuer.getUserId();
        WithdrawalSubmitReq data = req.getData();
        data.setUserId(userId);
        return withdrawalService.withdrawalSubmit(data);
    }



    @PostMapping("/withdrawalRecord")
    @ApiOperation("提现申请记录")
    @NeedLogin
    public Resp<List<WithdrawalVo>> withdrawalRecord(@RequestBody Req<WithdrawalRecordReq> req){
        SessionUser issuer = LoginContext.getLoginUser();
        Long userId = issuer.getUserId();
        WithdrawalRecordReq data = req.getData();
        data.setUserId(userId);
        return withdrawalService.withdrawalRecord(data);
    }

}
