package com.xishi.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cloud.webcore.service.RedisService;
import com.common.base.model.Resp;
import com.common.base.util.*;
import com.component.cos.CosHelper;
import com.xishi.user.dao.mapper.InvitationCodeMapper;
import com.xishi.user.dao.mapper.UserWalletMapper;
import com.xishi.user.entity.constant.RedisConstants;
import com.xishi.user.model.pojo.*;
import com.xishi.user.entity.req.AccountReq;
import com.xishi.user.entity.req.ChangePwdReq;
import com.xishi.user.entity.vo.UserInfoVo;
import com.xishi.user.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

@Slf4j
@Service
public class AccountServiceImpl implements IAccountService {

    private static final String avatarDefaultPath="http://nahanhb1.oss-cn-qingdao.aliyuncs.com/resources/movie_cover/acaf8649804c4c01b20b8a12709d9588.png";

    @Autowired
    private IUserService userService;

    @Autowired
    private ISmsService smsService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private UserWalletMapper userWalletMapper;

    @Autowired
    private InvitationCodeMapper invitationCodeMapper;

    @Autowired
    private IInvitationCodeService invitationCodeService;

    @Autowired
    private IUnbindDeviceService unbindDeviceService;

    @Autowired
    private CosHelper cosHelper;

    //注册用户
    @Transactional
    @Override
    public User registerUser(AccountReq accountReq,InvitationCode invitationCode) {
        String phone = accountReq.getAccount(); //手机号
        log.info("AccountServiceImpl registerUser start,phone={}",phone);
        //三方登录opendId
        String openId = accountReq.getThirdAccount();
        //0 qq 1 wx
        int thirdType = accountReq.getThirdType()==null?0:accountReq.getThirdType();
        int regType = accountReq.getRegType(); //注册来源

        String pwd = accountReq.getPwd();
        Map<String, String> pwdMap = ToolUtil.pwdEncrypt(pwd);

        User userInsert = new User();
        userInsert.setPhone(phone);
        userInsert.setPassword(pwdMap.get("pwd"));
        userInsert.setSalt(pwdMap.get("salt"));

        //默认头像
        String defAvatar =avatarDefaultPath;
        userInsert.setAvatar(defAvatar);
        userInsert.setIntro("这个人很懒,什么都没留下~");
        userInsert.setSource(regType);
        Date now = new Date();

        int age = DateTimeKit.ageOfNow(now);
        userInsert.setAge(age);
        userInsert.setBirthday(now);
        userInsert.setIntro("本宝宝暂时没有想到个性的签名~"); //签名
        userInsert.setCity(accountReq.getCity());
        //西施号
        String xishiNo = userService.genAccountNo();
        userInsert.setName("用户" + xishiNo);//用户名
        userInsert.setXishiNum(xishiNo);
        userInsert.setCreateTime(now);
        userInsert.setUpdateTime(now);
        //三方登录直接注册
        if (StrKit.isNotEmpty(openId)) {    //第三方账号
            if (thirdType == 0) {
                userInsert.setQq(openId);
            } else {
                userInsert.setWx(openId);
            }
        }
        if(invitationCode!=null) {
            //邀请人id
            Long inviterId = invitationCode.getUserId();
            User inviterUser = userService.getById(inviterId);
            userInsert.setInviterId(inviterId);
        }
//        //TODO 注册用户到币宝
//        boolean registBB = userService.registToBB(xishiNo);
//        if(!registBB){
//            return null;
//        }
        //TODO 获取币宝用户地址 1：会员  2：商户
//        boolean addressFromBB = userService.getAddressFromBB(xishiNo, 1);//用户地址
//        if(!addressFromBB) return null;
//
//        boolean exists = redisService.exists(RedisConstants.LIVE_AGENT_NO_BB);
//        if(!exists){
//            userService.getAddressFromBB(xishiNo,2);//商户地址
//        }
        //保存至数据库
        userService.save(userInsert);
        Long userId = userInsert.getId();
        if (userId == null || userId <= 0) {
            return null;
        }
        //创建钱包
        this.createUserWallet(userId);
        //创建邀请码
        invitationCodeService.createInvitationCode(userId, phone);
        InvitationUser inviteInfo =new InvitationUser();
        inviteInfo.setToUid(userId);
        if (invitationCode != null) {
            inviteInfo =invitationCodeService.inviteUser(invitationCode, userId);
        }
        return userInsert;
    }

    //创建用户钱包
    private void createUserWallet(Long userId) {
        UserWallet insert = new UserWallet();
        insert.setUserId(userId);
        insert.setGbMoeny(BigDecimal.ZERO);
        insert.setCreateTime(new Date());
        insert.setUpdateTime(new Date());
        this.userWalletMapper.insert(insert);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Resp<UserInfoVo> adminRegister(AccountReq accountReq) {
        String phone = accountReq.getAccount();
        User dbUser = userService.queryByPhone(phone);
        if (dbUser != null) {
            return Resp.error("该用户已存在");
        }
        String inviteCode = accountReq.getInviteCode();
        InvitationCode invitationCode = null;
        if (StrKit.isNotEmpty(inviteCode)) {
            invitationCode = this.invitationCodeMapper.selectByCode(inviteCode.trim());
            if (invitationCode == null) {
                return Resp.error("邀请码不存在");
            }
        }
        // 后台注册
        accountReq.setRegType(1);
        User user = registerUser(accountReq,invitationCode);
        if(user==null) {
            return Resp.error("添加失败,请稍后再试");
        }
        Long userId = user.getId();
        UserInfoVo userInfoVo = new UserInfoVo();
        userInfoVo.setUserId( userId);
        Resp<UserInfoVo> resp = new Resp<UserInfoVo>(userInfoVo);
        return resp;
    }

    //修改密码
    @Override
    public boolean modifyPwd(User user,ChangePwdReq changePwdReq) {
        log.info("AccountServiceImpl modifyPwd start,userId={},phone={}",user.getId(),user.getPhone());
        String newPwd = changePwdReq.getPwd();

        Map<String, String> pwdMap = ToolUtil.pwdEncrypt(newPwd);
        User userUpt = new User();
        userUpt.setId(user.getId());
        userUpt.setPassword(pwdMap.get("pwd"));
        userUpt.setSalt(pwdMap.get("salt"));
        userUpt.setUpdateTime(new Date());
        boolean rt = userService.updateById(userUpt);
        return rt;
    }

    //修改交换密码
    @Override
    public boolean modifyPayPwd(User user,ChangePwdReq changePwdReq) {
        log.info("AccountServiceImpl modifyPayPwd start,userId={},phone={}",user.getId(),user.getPhone());
        String payPwd = changePwdReq.getPwd();
        String enPwd = MD5Password.md5(payPwd);
        User userUpt = new User();
        userUpt.setId(user.getId());
        userUpt.setPayPwd(enPwd);
        userUpt.setUpdateTime(new Date());
        boolean rt =userService.updateById(userUpt);
        return rt;
    }

    //第三方账号绑定手机号
    @Override
    public Resp<Void> bind(AccountReq accountReq) {
        String account = accountReq.getAccount();
        String thirdAccount = accountReq.getThirdAccount();
        int thirdType = accountReq.getThirdType()==null?0:accountReq.getThirdType();

//        Object o = redisService.get(SystemConstants.SMS_PUBLIC_KEY.concat(account));
//        if (null == o) return Resp.error("验证不通过,请重新获取验证码");

        User dbUser;
        User dbThreeUser;

        if (thirdType == 0) {
            dbThreeUser = userService.getOne(new QueryWrapper<User>().eq("qq", thirdAccount));
        } else {
            dbThreeUser = userService.getOne(new QueryWrapper<User>().eq("wx", thirdAccount));
        }
        if (dbThreeUser != null) {
            if (!dbThreeUser.getPhone().equals(account)) {
                return Resp.error("该账户已绑定其他手机号");
            }
            return Resp.error("该账户已绑定此手机号");
        }

        dbUser = userService.queryByPhone(account);
        if (dbUser == null) {
            if (StrKit.isEmpty(accountReq.getPwd())) {
                return Resp.error("请输入密码");
            }
            if (StrKit.isEmpty(thirdAccount)) {
                return Resp.error("参数错误,缺少OpenId");
            }
            dbUser= registerUser(accountReq,null);
            if(dbUser==null) {
                return Resp.error("网络异常,请稍后再试");
            }
//          redisService.remove(SystemConstants.SMS_PUBLIC_KEY.concat(account));
            return Resp.success("绑定成功");
        } else {
            if (thirdType == 0) {
                if (StrKit.isNotEmpty(dbUser.getQq()) && !dbUser.getQq().equals(thirdAccount)) {
                    return Resp.error("该手机号已绑定其他账户");
                }
            } else {
                if (StrKit.isNotEmpty(dbUser.getWx()) && !dbUser.getWx().equals(thirdAccount)) {
                    return Resp.error("该手机号已绑定其他账户");
                }
            }

            Long userId = dbUser.getId();
//            redisService.remove(SystemConstants.SMS_PUBLIC_KEY.concat(account));

            User userUpt = new User();
            userUpt.setId(userId);
            userUpt.setUpdateTime(new Date());
            switch (thirdType) {
                //QQ
                case 0:
                    userUpt.setQq(thirdAccount);
                    break;
                //WX
                case 1:
                    userUpt.setWx(thirdAccount);
                    break;
            }
            boolean rt = userService.updateById(userUpt);
            return rt ? Resp.success("绑定成功") : Resp.error("网络异常,请稍后再试");
        }
    }

    //验证账号密码
    public boolean checkAccount(User user,String pwd) {
        String uPwd = user.getPassword();
        String uSalt = user.getSalt();
        log.info("密码验证:pwd=[{}]",pwd);
        log.info("密码验证:salt=[{}]",uSalt);
        String inPwd = ToolUtil.pwdEncrypt(pwd, uSalt);
        log.info("密码验证：uPwd==[{}],inpwd=[{}]",uPwd,inPwd);
        if (StringUtils.isBlank(uPwd) || !uPwd.equals(inPwd)) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        String pwd = "a123456";
//        Map<String, String> stringStringMap = ToolUtil.pwdEncrypt(pwd);
//        String upwd = stringStringMap.get("pwd").toString();
//        String salt = stringStringMap.get("salt").toString();
//        System.out.println("upwd====="+upwd);
//        System.out.println("salt====="+salt);
        String s = ToolUtil.pwdEncrypt(pwd, "a0202e63f4");

        pwd = pwd.toUpperCase();
        String s2 = ToolUtil.pwdEncrypt(pwd, "a0202e63f4");

        System.out.println("s ======"+s);
        System.out.println("s2======"+s2);
    }



    //解除设备绑定
    public Resp<Void> unbindDevice(User user) {
        Long userId = user.getId();
        String phone = user.getPhone();
        log.info("AccountServiceImpl unbindDevice start,解除设备绑定,userId={},phone={}",userId,phone);
        Date now = new Date();
        Calendar c = Calendar.getInstance();
        Integer year =c.get(Calendar.YEAR);
        UnbindDevice unbindDevice = new UnbindDevice();
        unbindDevice.setUserId(userId);
        unbindDevice.setCreateTime(now);
        unbindDevice.setUpdateTime(now);
        unbindDevice.setDoYear(year);
        unbindDeviceService.save(unbindDevice);

        User userUpt = new User();
        userUpt.setId(userId);
        userUpt.setUpdateTime(now);
        boolean rt=userService.updateById(userUpt);
        if(!rt) {
            return Resp.error("解绑失败，请稍后重试");
        }
        return Resp.success("解绑成功");
    }
}
