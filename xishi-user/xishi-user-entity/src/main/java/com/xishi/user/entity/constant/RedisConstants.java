package com.xishi.user.entity.constant;

/**
 * 系统常量
 */
public class RedisConstants {

    /**
     * 用户点赞
     */
    public static final String USER_DYNAMIC_PRAISE = "user_dynamic_praise_";

    /**
     * 用户同意加入家族 锁
     */
    public static final String AGREE_AGENT_INVITE = "agree_agent_invite_";

    /**
     * 用户赠送礼物 锁
     */
    public static final String USER_SEND_GIFT = "user_send_gift_";
    /**
     * 代理商增加余额 锁
     */
    public static final String AGENT_MONEY = "agent_money_";
    /**
     * 全员禁言 锁
     */
    public static final String USER_STOP_SEND_MESSAGE = "user_stop_send_message";

    /**
     * 本场直播贡献值 缓存
     */
    public static final String LIVE_TOTAL_AMOUNT = "Live_total_amount:";
    /**
     * 用户是否加入本场直播 缓存
     */
    public static final String LIVE_USER_ADD = "live_user_add:";
    /**
     * 本场直播在线人数 缓存
     */
    public static final String LIVE_USER_NUM = "live_user_num:";
    /**
     * 常规收费 本场直播支付凭证 缓存
     */
    public static final String LIVE_USER_PAYED = "live_user_payed:";

    /**
     * 用户被踢出房间 缓存
     */
    public static final String LIVE_USER_GET_OUT = "live_user_get_out:";
    /**
     * 用户被禁言 缓存
     */
    public static final String LIVE_USER_NO_SNED = "live_user_no_sned:";

    /**
     * 用户币宝地址 缓存
     */
    public static final String LIVE_USER_NO_BB = "live_user_no_bb:";

    /**
     * 商户币宝地址 缓存
     */
    public static final String LIVE_AGENT_NO_BB = "live_agent_no_bb:";

    /**
     * 本场直播用户列表 缓存
     */
    public static final String LIVE_USER_LIST = "live_user_list:";
    /**
     * 本场直播用户观看计时 缓存
     */
    public static final String LIVE_USER_TIME_COUNT = "live_user_time_count:";
    /**
     * 本场直播用户观看计时 缓存
     */
    public static final String LIVE_USER_IN_ROOM = "live_user_in_room:";

  /**
     * 本场直播用户观看计时 缓存
     */
    public static final String USER_PAY_MOVIE = "user_pay_movie_";

    /**
     * 输入密码进入主播间 缓存
     */
    public static final String LIVE_USER_PWD = "live_user_pwd:";


}
