package com.xishi.user.service;

import com.cloud.webcore.service.BaseLoginService;
import com.common.base.model.Resp;
import com.xishi.user.entity.req.LoginReq;
import com.xishi.user.entity.vo.LoginResultVo;

public interface ILoginService extends BaseLoginService{

    //登录
    public Resp<LoginResultVo> login(LoginReq loginReq);

    //登出
    public boolean logout(Long userId);

    //检查登录态
    public boolean checkLogin(Long userId);

    //根据token检查用户登录状态
    public boolean checkLoginByToken(Long userId,String token);

    //延期token
    public boolean deferToken(Long userId,String token);

    //刷新用户token
    public String refreshToken(Long userId);

    void offline(Long userId);

    void online(Long userId);

    //添加访问量
    void addVisitor();
}
