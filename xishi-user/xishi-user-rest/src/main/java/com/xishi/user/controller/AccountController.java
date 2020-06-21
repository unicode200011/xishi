package com.xishi.user.controller;

import com.cloud.webcore.annotation.NeedLogin;
import com.cloud.webcore.context.LoginContext;
import com.cloud.webcore.model.SessionUser;
import com.cloud.webcore.service.RedisService;
import com.common.base.model.Req;
import com.common.base.model.Resp;
import com.common.base.util.*;
import com.xishi.user.entity.constant.SystemConstants;
import com.xishi.user.entity.req.ChangePhoneReq;
import com.xishi.user.entity.req.UserPayPwdReq;
import com.xishi.user.model.pojo.InvitationCode;
import com.xishi.user.model.pojo.User;
import com.xishi.user.entity.req.AccountReq;
import com.xishi.user.entity.req.ChangePwdReq;
import com.xishi.user.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/account")
@Slf4j
@Api(value = "用户账号接口", description = "用户账号接口")
public class AccountController {

    private static String pwdPattern="^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,18}$";

    @Autowired
    private IAccountService accountService;

    @Autowired
    private IUserService userService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ISmsService smsService;

    @Autowired
    private IInvitationCodeService invitationCodeService;

    @Autowired
    private IUnbindDeviceService unbindDeviceService;

    @PostMapping("/register")
    @ApiOperation("用户注册")
    public Resp<Void> register(@RequestBody @Valid Req<AccountReq> req, BindingResult bindingResult) {
        AccountReq accountReq = req.getData();
        String phone = accountReq.getAccount();
        String pwd = accountReq.getPwd();
        log.info("AccountController register start,phone={}", phone);
        if (bindingResult.hasErrors()) {
            return Resp.error(bindingResult.getFieldError().getDefaultMessage());
        }
        if (StrKit.isEmpty(pwd)) {
            return Resp.error("密码不能为空");
        }
        boolean pwdCheck =checkPwd(pwd);
        if(!pwdCheck) {
            return Resp.error("密码格式不对");
        }
        if (StringUtils.isBlank(phone) || !ToolUtil.isPhone(phone)) {
            return Resp.error("手机号码错误");
        }
        User dbUser = userService.queryByPhone(phone);
        if (dbUser != null) {
            return Resp.error("该手机号已注册");
        }
        String code = accountReq.getCode();
        boolean codeCheck = smsService.checkCode(phone, code);
        if (!codeCheck) {
            return Resp.error("验证码错误");
        }
        InvitationCode invitationCode = null;
        String inviteCode = accountReq.getInviteCode();
//        if (StrKit.isEmpty(inviteCode)) {
//            return Resp.error("邀请码不能为空");
//        }
        if (StringUtils.isNotBlank(inviteCode)) {
            invitationCode = invitationCodeService.queryByCode(inviteCode);
            if (invitationCode == null) {
                return Resp.error("邀请码不存在");
            }
        }
        User user = accountService.registerUser(accountReq, invitationCode);
        if (user == null) {
            return Resp.error("网络异常,请稍后再试");
        }
        return Resp.success("注册成功");
    }

    @PostMapping("/inviteRegister")
    @ApiOperation("邀请码注册")
    public Resp<Void> inviteRegister(@RequestBody @Valid Req<AccountReq> req, BindingResult bindingResult) {
        AccountReq accountReq = req.getData();
        String phone = accountReq.getAccount();
        String pwd = accountReq.getPwd();
        String inviteCode = accountReq.getInviteCode();
        log.info("AccountController inviteRegister start,phone={}", phone);
        if (bindingResult.hasErrors()) {
            return Resp.error(bindingResult.getFieldError().getDefaultMessage());
        }
        if (StrKit.isEmpty(pwd)) {
            return Resp.error("密码不能为空");
        }
        boolean pwdCheck =checkPwd(pwd);
        if(!pwdCheck) {
            return Resp.error("密码格式不对");
        }
        if (StringUtils.isBlank(phone) || !ToolUtil.isPhone(phone)) {
            return Resp.error("手机号码错误");
        }
        if (StrKit.isEmpty(inviteCode)) {
            return Resp.error("邀请码不能为空");
        }
        InvitationCode invitationCode = invitationCodeService.queryByCode(inviteCode);
        if (invitationCode == null) {
            return Resp.error("邀请码不存在");
        }
        User dbUser = userService.queryByPhone(phone);
        if (dbUser != null) {
            return Resp.error("该手机号已注册");
        }
        String code = accountReq.getCode();
        boolean codeCheck = smsService.checkCode(phone, code);
        if (!codeCheck) {
            return Resp.error("验证码错误");
        }
        User user = accountService.registerUser(accountReq, invitationCode);
        if (user == null) {
            return Resp.error("网络异常,请稍后再试");
        }
        return Resp.success("注册成功");
    }

    private boolean checkPwd(String pwd) {
        if(StringUtils.isBlank(pwd)) {
            return false;
        }
        int length= pwd.length();
        if(length<6 || length>18) {
            return false;
        }
        if(!pwd.matches(pwdPattern)) {
            return false;
        }
        return true;
    }

    @PostMapping("/modifyPwd")
    @ApiOperation("修改登录密码")
    @NeedLogin
    public Resp<Void> modifyPwd(@RequestBody Req<ChangePwdReq> req) {
        ChangePwdReq changePwdReq = req.getData();
        String newPwd = changePwdReq.getPwd();
        String confirmPwd = changePwdReq.getConfirmPwd();
        String oldPwd = changePwdReq.getOldPwd();
        if (StringUtils.isBlank(oldPwd)) {
            return Resp.error("请输入原密码");
        }
        if (StringUtils.isBlank(newPwd) || StringUtils.isBlank(confirmPwd)) {
            return Resp.error("请输入密码");
        }
        boolean pwdCheck =checkPwd(newPwd);
        if(!pwdCheck) {
            return Resp.error("密码格式不对");
        }
        if (!newPwd.equals(confirmPwd)) {
            return Resp.error("新密码与确认密码不一致");
        }
        SessionUser userVo = LoginContext.getLoginUser();
        Long userId = userVo.getUserId();
        changePwdReq.setUserId(userId);
        User user = userService.getById(userId);
        if (user == null) {
            return Resp.error("用户不存在");
        }
//        String code = changePwdReq.getCode();
//        boolean codeCheck = smsService.checkCode(user.getPhone(),code);
//        if(!codeCheck) {
//            return Resp.error("验证码错误");
//        }
        String orglPwd = user.getPassword();
        String salt = user.getSalt();
        String inPwd = ToolUtil.pwdEncrypt(oldPwd, salt);
        if (StringUtils.isBlank(orglPwd) || !orglPwd.equals(inPwd)) {
            log.info("AccountController modifyPwd check密码失败,user phone={}", user.getPhone());
            return Resp.error("原密码不正确");
        }
        boolean rt = accountService.modifyPwd(user, changePwdReq);
        return rt ? Resp.success("修改成功") : Resp.error("修改失败,请稍后再试");
    }

    @PostMapping("/resetPwd")
    @ApiOperation("重置登录密码")
    public Resp<Void> resetPwd(@RequestBody Req<ChangePwdReq> req) {
        ChangePwdReq changePwdReq = req.getData();
        String newPwd = changePwdReq.getPwd();
        String confirmPwd = changePwdReq.getConfirmPwd();
        if (StringUtils.isBlank(newPwd) || StringUtils.isBlank(confirmPwd)) {
            return Resp.error("请输入密码");
        }
        boolean pwdCheck =checkPwd(newPwd);
        if(!pwdCheck) {
            return Resp.error("密码格式不对");
        }
        if (!newPwd.equals(confirmPwd)) {
            return Resp.error("新密码与确认密码不一致");
        }
        String phone = changePwdReq.getPhone();
        User user = userService.queryByPhone(phone);
        if (user == null) {
            return Resp.error("用户不存在");
        }
//        Object o = redisService.get(SystemConstants.SMS_FIND_FIND_SET_PASS_KEY.concat(phone));
//        if (null == o) return Resp.error("验证不通过,请重新获取验证码");
//        redisService.remove(SystemConstants.SMS_FIND_FIND_SET_PASS_KEY.concat(phone));

        String code = changePwdReq.getCode();
        boolean codeCheck = smsService.checkCode(phone,code);
        if(!codeCheck) {
            return Resp.error("验证码错误");
        }
        boolean rt = accountService.modifyPwd(user, changePwdReq);
        return rt ? Resp.success("密码找回成功") : Resp.error("密码找回失败,请稍后再试");
    }

    @PostMapping("/modifyPayPwd")
    @ApiOperation("修改支付密码")
    @NeedLogin
    public Resp<Void> modifyPayPwd(@RequestBody Req<ChangePwdReq> req) {
        ChangePwdReq changePwdReq = req.getData();
        String newPwd = changePwdReq.getPwd();
        String confirmPwd = changePwdReq.getConfirmPwd();
        String oldPwd = changePwdReq.getOldPwd();
        if (StringUtils.isBlank(oldPwd)) {
            return Resp.error("请输入原支付密码");
        }
        if (StringUtils.isBlank(newPwd) || StringUtils.isBlank(confirmPwd)) {
            return Resp.error("请输入新支付密码");
        }
        if (!newPwd.equals(confirmPwd)) {
            return Resp.error("新密码与确认密码不一致");
        }
        SessionUser userVo = LoginContext.getLoginUser();
        Long userId = userVo.getUserId();
        changePwdReq.setUserId(userId);
        User user = userService.getById(userId);
        if (user == null) {
            return Resp.error("用户不存在");
        }
        String orglPayPwd = user.getPayPwd();
        String inPwd = MD5Password.md5(oldPwd);

        if (StringUtils.isBlank(orglPayPwd) || !orglPayPwd.equals(inPwd)) {
            log.info("AccountController exchangePwd check支付密码失败,user phone={}", user.getPhone());
            return Resp.error("原支付密码不正确");
        }
        boolean rt = accountService.modifyPayPwd(user, changePwdReq);
        return rt ? Resp.success("修改成功") : Resp.error("修改失败,请稍后再试");
    }

    @PostMapping("/setPayPwd")
    @ApiOperation("设置支付密码")
    @NeedLogin
    public Resp<Void> setPayPwd(@RequestBody Req<ChangePwdReq> req) {
        ChangePwdReq changePwdReq = req.getData();
        String newPwd = changePwdReq.getPwd();
        String confirmPwd = changePwdReq.getConfirmPwd();
        if (StringUtils.isBlank(newPwd) || StringUtils.isBlank(confirmPwd)) {
            return Resp.error("请输入支付密码");
        }
        if (!newPwd.equals(confirmPwd)) {
            return Resp.error("支付密码与确认密码不一致");
        }
        SessionUser userVo = LoginContext.getLoginUser();
        Long userId = userVo.getUserId();
        changePwdReq.setUserId(userId);
        User user = userService.getById(userId);
        if (user == null) {
            return Resp.error("用户不存在");
        }
        String payPwd = user.getPayPwd();
        if (StringUtils.isNotBlank(payPwd)) {
            return Resp.error("已设置过支付密码");
        }
        boolean rt = accountService.modifyPayPwd(user, changePwdReq);
        return rt ? Resp.success("设置成功") : Resp.error("设置失败,请稍后再试");
    }

    @PostMapping("/resetPayPwd")
    @ApiOperation("重置支付密码")
    public Resp<Void> resetPayPwd(@RequestBody Req<ChangePwdReq> req) {
        ChangePwdReq changePwdReq = req.getData();
        String newPwd = changePwdReq.getPwd();
        String confirmPwd = changePwdReq.getConfirmPwd();
        if (StringUtils.isBlank(newPwd) || StringUtils.isBlank(confirmPwd)) {
            return Resp.error("请输入支付密码");
        }
        if (!newPwd.equals(confirmPwd)) {
            return Resp.error("新支付密码与确认密码不一致");
        }
        String phone = changePwdReq.getPhone();
        User user = userService.queryByPhone(phone);
        if (user == null) {
            return Resp.error("用户不存在");
        }

        Object o = redisService.get(SystemConstants.SMS_FIND_FIND_SET_PASS_KEY.concat(phone));
        if (null == o) return Resp.error("验证不通过,请重新获取验证码");
        redisService.remove(SystemConstants.SMS_FIND_FIND_SET_PASS_KEY.concat(phone));

        boolean rt = accountService.modifyPayPwd(user, changePwdReq);
        return rt ? Resp.success("支付密码找回成功") : Resp.error("密码找回失败,请稍后再试");
    }

    @PostMapping("/validatePayPwd")
    @ApiOperation("验证支付密码")
    @NeedLogin
    public Resp<Void> validatePayPwd(@RequestBody Req<UserPayPwdReq> req) {
        String payPwd = req.getData().getPwd();

        if (StringUtils.isBlank(payPwd)) {
            return Resp.error("支付密码为空");
        }
        SessionUser userVo = LoginContext.getLoginUser();
        Long userId = userVo.getUserId();
        User user = userService.getById(userId);
        if (user == null) {
            return Resp.error("用户不存在");
        }
        String orglPayPwd = user.getPayPwd();
        String inPwd = MD5Password.md5(payPwd);


        if (StringUtils.isBlank(orglPayPwd) || !orglPayPwd.equals(inPwd)) {
            return Resp.error("支付密码不正确");
        }
        //redisService.set(SystemConstants.PAY_PWD_REDIS_KEY_PREFIX_.concat(userId.toString()), 1, SystemConstants.PAY_PWD_REDIS_TIME);
        return Resp.success();
    }

    @PostMapping("/bindPhone")
    @ApiOperation("第三方账号绑定手机号")
    public Resp<Void> bindPhone(@RequestBody @Valid Req<AccountReq> req) {
        AccountReq accountReq = req.getData();
        String phone = accountReq.getAccount();
        if (StringUtils.isBlank(phone)) {
            return Resp.error("手机号为空");
        }
        String code = accountReq.getCode();
        boolean codeCheck = smsService.checkCode(phone, code);
        if (!codeCheck) {
            return Resp.error("验证码错误");
        }
        Resp<Void> resp = accountService.bind(accountReq);
        return resp;
    }

    @NeedLogin
    @PostMapping("/bindPhoneOnline")
    @ApiOperation("第三方账号绑定手机号-登录状态下")
    public Resp<Void> bindPhoneOnline(@RequestBody @Valid Req<AccountReq> req) {
        AccountReq accountReq = req.getData();
        String phone = accountReq.getAccount();
        if (StringUtils.isBlank(phone)) {
            return Resp.error("手机号为空");
        }
        SessionUser userVo = LoginContext.getLoginUser();
        Long userId = userVo.getUserId();
        User user = userService.getById(userId);
        if (user == null) {
            return Resp.error("用户不存在");
        }
        Resp<Void> resp = accountService.bind(accountReq);
        return resp;
    }

    @PostMapping("/checkBind")
    @ApiOperation("检查是否绑定三方账户")
    @NeedLogin
    public Resp<Map> checkBind(@RequestBody Req<Void> req) {
        SessionUser userVo = LoginContext.getLoginUser();
        Long userId = userVo.getUserId();
        User user = userService.getById(userId);
        if (user == null) {
            return Resp.error("用户不存在");
        }

        return Resp.successData(MapUtil.build().put("qq", StrKit.isNotEmpty(user.getQq())).put("wx", StrKit.isNotEmpty(user.getWx())).over());
    }

    @PostMapping("/unbindAccount")
    @ApiOperation("解除绑定微信")
    @NeedLogin
    public Resp<Void> unbindAccount(@RequestBody @Valid Req<Void> req) {

        SessionUser userVo = LoginContext.getLoginUser();
        Long userId = userVo.getUserId();
        User user = userService.getById(userId);
        if (user == null) {
            return Resp.error("用户不存在");
        }
        String phone = user.getPhone();
        if(StringUtils.isEmpty(phone)){
            return Resp.error("用户未绑定手机号");
        }
        user.setWx("");
        user.setUpdateTime(new Date());
        return userService.updateById(user) ? Resp.success() : Resp.error("解绑失败,请稍后再试");
    }


    @PostMapping("/changePhone")
    @ApiOperation("更换绑定手机号")
    @NeedLogin
    public Resp<Void> changePhone(@RequestBody @Valid Req<ChangePhoneReq> req) {
        ChangePhoneReq accountReq = req.getData();
        String phone = accountReq.getPhone();
        if (StringUtils.isBlank(phone) || !ToolUtil.isPhone(phone)) {
            return Resp.error("手机号码错误");
        }
        SessionUser userVo = LoginContext.getLoginUser();
        Long userId = userVo.getUserId();
        User user = userService.getById(userId);
        if (user == null) {
            return Resp.error("用户不存在");
        }


        if (user.getPhone().equals(phone)) {
            return Resp.error("换绑手机不能是当前手机号");
        }
        User existUser = userService.queryByPhone(phone);
        if (existUser != null && !existUser.getId().equals(userId)) {
            return Resp.error("该新手机号已绑定其他账户");
        }

        //匹配原手机号验证码
        String oldCode = accountReq.getOldCode();
        boolean oldCodeCheck = smsService.checkCode(phone, oldCode);
        if (!oldCodeCheck) {
            return Resp.error("原手机号验证码错误");
        }
        //匹配新手机号验证码
        String code = accountReq.getCode();
        boolean codeCheck = smsService.checkCode(phone, code);
        if (!codeCheck) {
            return Resp.error("新手机验证码错误");
        }

        user = userService.getById(userId);
        user.setPhone(phone);
        user.setUpdateTime(new Date());
        return user.updateById() ? Resp.success() : Resp.error("换绑失败,请稍后再试");
    }


}
