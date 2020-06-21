package com.xishi.socket.entity.Enum;

import lombok.Getter;

@Getter
public class MqUserServiceTypeConstens {
    //用户断开连接
    public static final String USER_LOGOUT="user_logout";
    //用户连接
    public static final String USER_LOGIN="user_login";

    //用户等级
    public static final String USER_GRADE="user_grade";

    //直播等级
    public static final String LIVE_GRADE="live_grade";

    //直播常规模式扣费
    public static final String LIVE_PAY="live_pay";
}
