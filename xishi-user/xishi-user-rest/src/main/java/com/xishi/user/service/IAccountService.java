package com.xishi.user.service;

import com.common.base.model.Resp;
import com.xishi.user.model.pojo.InvitationCode;
import com.xishi.user.model.pojo.User;
import com.xishi.user.entity.req.AccountReq;
import com.xishi.user.entity.req.ChangePwdReq;
import com.xishi.user.entity.vo.UserInfoVo;

public interface IAccountService {

    //注册用户
    User registerUser(AccountReq accountReq,InvitationCode invitationCode);

    /**
     * 后台注册用户
     */
    Resp<UserInfoVo> adminRegister(AccountReq accountReq);

    /**
     * 修改密码
     */
    boolean modifyPwd(User user,ChangePwdReq changePwdReq);

    /**
     * 修改交换密码
     */
    boolean modifyPayPwd(User user,ChangePwdReq changePwdReq);

    /**
     * 绑定账户
     */
    Resp<Void> bind(AccountReq accountReq);

    //验证账号密码
    boolean checkAccount(User user,String pwd);

    //解除设备绑定
    Resp<Void> unbindDevice(User user);

}
