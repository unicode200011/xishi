package com.xishi.user.service.impl;

import com.cloud.webcore.config.JwtProperties;
import com.cloud.webcore.service.RedisService;
import com.cloud.webcore.util.JwtTokenUtil;
import com.common.base.model.Resp;
import com.common.base.util.RequestUtil;
import com.common.base.util.ToolUtil;
import com.xishi.user.entity.constant.SystemConstants;
import com.xishi.user.enums.BizEnum;
import com.xishi.user.model.pojo.User;
import com.xishi.user.entity.req.LoginReq;
import com.xishi.user.entity.vo.LoginResultVo;
import com.xishi.user.service.IAccountService;
import com.xishi.user.service.ILoginService;
import com.xishi.user.service.ISmsService;
import com.xishi.user.service.IUserService;
import com.xishi.user.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Service
@Slf4j
public class LoginServiceImpl implements ILoginService {

    @Autowired
    private IUserService userService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ISmsService smsService;

    @Autowired
    private IAccountService accountService;

    public Resp<LoginResultVo> login(LoginReq loginReq) {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String ipAddress = RequestUtil.getIpAddress(request);
        log.info("login send start,ipAddress={}",ipAddress);

        String account = loginReq.getAccount();
        log.info("LoginServiceImpl login start, account={}", account);
        User dbUser = null;
        Integer loginType = loginReq.getLoginType();
        switch (loginType) {
            case LoginReq.ACCOUNT_LOGIN:
                if (!ToolUtil.phoneRegTwo(account)) {
                    return Resp.error("手机号格式错误");
                }
                String pwd = loginReq.getPwd();
                dbUser = userService.queryByPhone(account);
                if(dbUser==null) {
                    return Resp.error("用户不存在");
                }
                boolean accountCheck = accountService.checkAccount(dbUser, pwd);
                if (!accountCheck) {
                    log.info("LoginServiceImpl login fail,登陆失败，用户名或密码不正确,手机号={}", dbUser.getPhone());
                    return Resp.error("请输入正确的手机号或密码");
                }
                break;
            case LoginReq.OTHER_LOGIN:
                int thirdType = loginReq.getThirdType() == null ? 0 : loginReq.getThirdType();
                User userCdn = new User();
                if (thirdType == 0) {
                    userCdn.setQq(account);
                } else {
                    userCdn.setWx(account);
                }
                dbUser = userService.queryByThirdAccount(account,thirdType);
                if (dbUser == null) {
                    return new Resp(BizEnum.BANDING_PHONE.getCode(), "请绑定手机号");
                }
                break;
            case LoginReq.CODE_LOGIN:
//                Object o = redisService.get(SystemConstants.SMS_LOGIN_KEY.concat(account));
//                if (null == o) return Resp.error("验证不通过,请重新获取验证码");
                boolean codeCheck = smsService.checkCode(account, loginReq.getPwd());
                if (!codeCheck) {
                    return Resp.error("验证码错误");
                }
                dbUser = this.userService.queryByPhone(account);
                if(dbUser==null) {
                    return Resp.error("用户不存在");
                }
                break;
            default:
                log.info("LoginServiceImpl login fail,登陆失败，请选择登录方式,loginType={}.account={}", loginType, account);
                return Resp.error("请选择登录方式");
        }
        Resp<Void> checkResp = userService.checkUser(dbUser);
        if (!checkResp.isSuccess()) {
            log.info("LoginServiceImpl login fail,登陆失败，用户不存在或状态异常,account={}", account);
            return new Resp<LoginResultVo>(checkResp.getCode(),checkResp.getMsg());
        }
        Long userId = dbUser.getId();
        final String randomKey = jwtTokenUtil.getRandomKey();
        final String token = jwtTokenUtil.generateToken(dbUser.getPhone(), userId, randomKey);

        String loginTokenKey = SystemConstants.LOGIN_TOKEN_PRE + userId;
        redisService.set(loginTokenKey, token, jwtProperties.getExpiration());

        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
        response.setHeader(jwtTokenUtil.getTokenKey(), token);

        User userUpt = new User();
        userUpt.setId(dbUser.getId());
        userUpt.setLoginTime(new Date());
        userUpt.setIp(ipAddress);
        userUpt.setOnlineState(1);
        userUpt.setUpdateTime(new Date());

        userService.updateById(userUpt);

        LoginResultVo loginResultVo = new LoginResultVo(dbUser.getId(), dbUser.getPhone());
        loginResultVo.setFirstLogin(dbUser.getFirstLogin());
        Resp<LoginResultVo> resp = new Resp<LoginResultVo>(loginResultVo);
        this.addVisitor();
        return resp;
    }

    //登出
    @Override
    public boolean logout(Long userId) {
        log.info("LoginServiceImpl logout start, userId={}", userId);
        String loginTokenKey = SystemConstants.LOGIN_TOKEN_PRE + userId;
        redisService.remove(loginTokenKey);
        this.offline(userId);
        return true;
    }

    //检查登录态
    @Override
    public boolean checkLogin(Long userId) {
        String loginTokenKey = SystemConstants.LOGIN_TOKEN_PRE + userId;
        Object cacheObj = redisService.get(loginTokenKey);
        String cacheToken = cacheObj == null ? null : cacheObj.toString();
        if (StringUtils.isNotBlank(cacheToken)) {
            return true;
        }
        return false;
    }

    //根据token检查用户登录状态
    @Override
    public boolean checkLoginByToken(Long userId, String token) {
        String loginTokenKey = SystemConstants.LOGIN_TOKEN_PRE + userId;
        Object cacheObj = redisService.get(loginTokenKey);
        String cacheToken = cacheObj == null ? null : cacheObj.toString();
        if (StringUtils.isNotBlank(cacheToken) && cacheToken.equals(token)) {
            return true;
        }
        return false;
    }

    public String refreshToken(Long userId) {
        User user = userService.getById(userId);
        String randomKey = jwtTokenUtil.getRandomKey();
        String token = jwtTokenUtil.generateToken(user.getPhone(), userId, randomKey);

        String loginTokenKey = SystemConstants.LOGIN_TOKEN_PRE + userId;
        redisService.set(loginTokenKey, token, jwtProperties.getExpiration());

        return token;
    }

    @Override
    public void offline(Long userId) {
        User byId = userService.getById(userId);
        if(byId != null){
            byId.setOnlineState(0);
            byId.setUpdateTime(new Date());
            userService.updateById(byId);
        }
    }

    @Override
    public void online(Long userId) {
        //this.addVisitor();
        User byId = userService.getById(userId);
        if(byId != null){
            byId.setOnlineState(1);
            byId.setUpdateTime(new Date());
            userService.updateById(byId);
        }
    }

    @Override
    public void addVisitor(){
        //统计总量
        Object userLoginNum = redisService.get("userLoginNum");
        System.out.println("userLoginNum:" + userLoginNum);
        if(userLoginNum == null){
            redisService.set("userLoginNum",1);
        }else {
            int newNum = (int)userLoginNum + 1;
            redisService.set("userLoginNum",newNum);
        }
        System.out.println("userLoginNum:" + redisService.get("userLoginNum"));
        //统计当天访问量
        Object todayLoginNum = redisService.get(DateUtils.getDate()+"userLoginNum");
        if(todayLoginNum == null){
            redisService.set(DateUtils.getDate()+"userLoginNum",1,30 * 24 * 60 * 60L);
        }else {
            int newNum = (int)todayLoginNum + 1;
            redisService.set(DateUtils.getDate()+"userLoginNum",newNum,30 * 24 * 60 * 60L);
        }

    }

    //延期token
    public boolean deferToken(Long userId, String token) {
        String loginTokenKey = SystemConstants.LOGIN_TOKEN_PRE + userId;
        Object cacheObj = redisService.get(loginTokenKey);
        String cacheToken = cacheObj == null ? null : cacheObj.toString();
        if (cacheToken != null && !cacheToken.equals(token)) {
            log.info("LoginServiceImpl deferToken 缓存token与入参token不一样，可疑调用,userId={}", userId);
        }
        redisService.set(loginTokenKey, token, jwtProperties.getExpiration());
        return true;
    }
}
