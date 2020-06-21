package com.xishi.user.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.cloud.webcore.model.UserBase;
import com.common.base.model.Resp;
import com.xishi.user.entity.req.*;
import com.xishi.user.model.PayResponInfo;
import com.xishi.user.model.pojo.User;
import com.xishi.user.entity.vo.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface IUserService extends IService<User> {

    /**
     * 缓存获取用户
     */
    User getFromCache(Long userId);

    /**
     * 重载缓存
     */
    void reloadUserToCache(Long userId);

    /**
     * 用户个人资料
     */
    UserInfoVo userInfo(Long userId);

    /**
     * 更新用户个人资料
     */
    Resp<Void> updateUserInfo(User user);

    /**
     * 推广链接界面数据
     */
    TuiGuangVo tuiGuangData(Long userId);

    /**
     * 实名认证数据回显
     */
    UserAuthVo userAuthInfo(Long userId);


    User queryByPhone(String phone);

    //身份认证通过
    boolean identifyAuthPass(Long userId);

    //搜索用户
    List<UserInfoVo> queryUserList(UserSearchQuery query);

    //生成西施号
    public String genAccountNo();

    //根据西施号查询用户
    public User queryByNo(String accountNo);

    User queryByThirdAccount(String account, Integer thirdType);
    //修改等级
    public boolean changeGrade(ChangeGradeReq gradeReq);

    Resp<Void> checkUser(User user);

    void getTast(GetTaskReq gradeReq);

    Resp<Void> applyUserAuthInfo(AuthRequestVo data);

    Resp<UserAuthVo> getUserAuthInfo(Long userId);

    Resp<UserAuthVo> updateUserAuthInfo(AuthRequestVo data);

    boolean checkLevel(Long userId, Integer level);

    Resp<UserGradeDetailVo> getUserGradeDetail(Long userId);

    Resp<UserGradeDetailVo> getUserLiveGradeDetail(Long userId);

    void changePraiseNum(Long id, Integer type);

    void changeAttentionNum(Long id, Integer type);

    void changeFansNum(Long id, Integer type);

    UserInfoVo otherUserInfo(UserReq data);

    Resp<Integer> checkAttention(UserAttentionQuery query);

    boolean registToBB(String xishiNo);

    boolean getAddressFromBB(String xishiNo,Integer type);

    Resp<Map> getLoginUrlFromBB(BBLoginReq bbLoginReq);

    String getCommentLevel(Integer id);

    //判断是否有支付密码
    Resp<Integer> isPayPwd(Long id);



}
