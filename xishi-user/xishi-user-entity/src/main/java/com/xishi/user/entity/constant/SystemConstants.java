package com.xishi.user.entity.constant;

import java.math.BigDecimal;

/**
 * 系统常量
 */
public class SystemConstants {

    /**
     * 默认编码格式
     */
    public static final String DEFAULT_CHARSET = "UTF-8";
    /**
     * spring cache
     */
    public static final String VIDEO_COMMENT_CACHE = "video:comment";
    public static final String VIDEO_COMMENT_REPLY_CACHE = "video:comment:reply";
    public static final String USER_INDEX_INFO_CACHE = "user:index:info";
    /**
     * 领取牛角-service
     */
    public static final String NJ_RECEIVE_SERVICE_LOCK = "nj_receive_service_lock_";
    /**
     * 领取牛角
     */
    public static final String NJ_RECEIVE_LOCK = "nj_receive_lock_";
    /**
     * 实名支付缓存
     */
    public static final String USER_AUTH_PAY_KEY = "user:auth:pay_";
    /**
     * 三网实名支付缓存
     */
    public static final String USER_AUTH_WEB_PAY_KEY = "user:authWeb:pay_";
    /**
     * 关键字屏蔽key
     */
    public static final String KEYWORD_FORBIDDEN_KEY = "keyword_forbidden_key_";
    /**
     * 抖牛 号
     */
    public static final String EXIST_DN_NUM = "user:register:existDnNum:num:";

    /**
     * 直播分类
     */
    public static final String LIVE_CATEGORY = "live_category";
    /**
     * 后台禁播
     */
    public static final String LOCK_END_LIVE_ADMIN = "END_LIVE_ADMIN_USER_ID_";
    /**
     * 开始直播
     */
    public static final String LOCK_START_LIVE = "lock_start_live_";
    /**
     * 结束直播
     */
    public static final String LOCK_END_LIVE = "lock_end_live_";
    /**
     * 直播列表 LIVE_LIST_R_CACHE:只读缓存 LIVE_LIST_R_W_CACHE:读写缓存
     */
    public static final String LIVE_LIST_R_CACHE = "live_read_list";
    public static final String LIVE_LIST_R_W_CACHE = "live_read_write_list";
    /**
     * 直播连麦者数量
     */
    public static final int LIVE_PUSHER_NUM = 3;
    /**
     * 直播管理器 房间
     */
    public static final String LIVE_ROOM_KEY = "live:liveRoom:liveId_";
    /**
     * 直播
     */
    public static final String LIVE = "live:live:liveId_";
    /**
     * 直播记录
     */
    public static final String LIVE_RECORD = "live:liveRecord:liveId_";
    /**
     * 直播数据
     */
    public static final String LIVE_DATA = "live:data:roomData:liveId_";
    /**
     * 直播送礼用户
     */
    public static final String LIVE_SEND_GIFT_USER = "live:data:sendGiftUser:liveId_";
    /**
     * 直播观看人数
     */
    public static final String LIVE_ONLINE_PERSON = "live:data:onlinePerson:liveId_";
    /**
     * 直播被踢出用户名单
     */
    public static final String LIVE_KICK_OUT_PERSON = "live:data:kickOut:liveId_";
    /**
     * 直播管理员
     */
    public static final String LIVE_ADMIN = "live:liveAdmin:liveId_";
    /**
     * 用户点赞记录-用于统计牛气值
     */
    public static final String VIDEO_PRAISE_4_NQ = "dn_video:praise4Nq:userId_";
    /**
     * 缓存Redis Key
     */
    //缓存失效时间 120秒
    public static final long CACHE_EXPIRE_TIME = 10L;
    //平台公共参数
    public static final String COMMEN_PARAM_REDIS_KEY = "dn_plat:commonParam:id:";
    //短视频
    public static final String VIDEO_REDIS_KEY = "dn_video:video:videoId:";
    //评论 针对所有用户缓存
    public static final String VIDEO_COMMENT_REDIS_KEY = "dn_video:comment:vid-p-r:";
    //回复 针对所有用户缓存
    public static final String VIDEO_COMMENT_REPLY_REDIS_KEY = "dn_video:comment:reply:commentId:";
    //回复 最新回复缓存
    public static final String VIDEO_COMMENT_NEW_REPLY_REDIS_KEY = "dn_video:comment:newReply:commentId:";
    //回复数量
    public static final String VIDEO_COMMENT_REPLY_NUM_REDIS_KEY = "dn_video:comment:replyNum:commentId:";
    //视频点赞 键+uid+"-"+vid
    public static final String VIDEO_PRAISE_REDIS_KEY = "dn_video:praise:uid-vid:";
    //视频收藏
    public static final String VIDEO_COLLECT_REDIS_KEY = "dn_video:collect:uid-vid:";
    //视频点赞数
    public static final String VIDEO_PRAISE_NUM_REDIS_KEY = "dn_video:praiseNum:videoId:";
    //评论数
    public static final String VIDEO_COMMENT_NUM_REDIS_KEY = "dn_video:commentNum:videoId:";
    //视频观看数
    public static final String VIDEO_WATCH_NUM_REDIS_KEY = "dn_video:watchNum:videoId:";
    //视频分享数
    public static final String VIDEO_SHARE_NUM_REDIS_KEY = "dn_video:shareNum:videoId:";
    //评论点赞数
    public static final String VIDEO_COMMENT_PRAISE_NUM_REDIS_KEY = "dn_video:commentPraiseNum:commentId:";
    //评论点赞
    public static final String VIDEO_COMMENT_PRAISE_REDIS_KEY = "dn_video:commentPraise:uid-cid:";
    //用户
    public static final String USER_REDIS_KEY = "dn_user:user:userId:";
    //用户关注
    public static final String USER_ATTENTION_KEY = "dn_user:attention:fromUid-toUid:";
    //用户关注的人列表
    public static final String USER_ATTENTION_LIST_REDIS_KEY = "dn_user:userAttentionList:userId:";
    //粉丝数,
    public static final String USER_FANS_NUM_KEY = "dn_user:fansNum:userId:";
    //关注数
    public static final String USER_ATTENTION_NUM_KEY = "dn_user:attentionNum:userId:";
    //获赞数
    public static final String USER_PRAISE_NUM_KEY = "dn_user:praiseNum:userId:";
    //用户观看记录
    public static final String USER_WATCH_RECORD_REDIS_KEY = "dn_video:userWatchRecord:userId:";
    //短视频预加载-根据用户
    public static final String PRE_LOAD_VIDEO_REDIS_CITY_KEY = "dn_video:preLoadVideo:city:";
    //短视频封面宽高
    public static final String VIDEO_COVER_DATA_KEY = "dn_video:cover:kg:videoId:";

    //名族
    public static final String NATION_REDIS_KEY = "nation:list";

    /**
     * rocket mq 短信
     */
    public static final String ROCKET_MQ_SMS_TOPIC = "douniu_sms";
    public static final String ROCKET_MQ_SMS_PROVIDER = "GID_douniu_sms";
    public static final String ROCKET_MQ_SMS_CONSUMER = "GID_douniu_sms";
    public static final String ROCKET_MQ_SMS_TAG = "sms_tag";
    /**
     * rocket mq 短视频
     */
    public static final String ROCKET_MQ_VIDEO_TOPIC = "douniu_video";
    public static final String ROCKET_MQ_VIDEO_PROVIDER = "GID_douniu_video";
    public static final String ROCKET_MQ_VIDEO_CONSUMER = "GID_douniu_video";
    public static final String ROCKET_MQ_VIDEO_TAG = "video_tag";
    public static final String ROCKET_MQ_VIDEO_PRAISE_KEY = "video_praise";
    public static final String ROCKET_MQ_VIDEO_COMMENT_PRAISE_KEY = "video_comment_praise";
    public static final String ROCKET_MQ_VIDEO_WORD_COMMENT_KEY = "video_comment";
    public static final String ROCKET_MQ_VIDEO_COMMENT_REPLY_KEY = "video_comment_reply";
    public static final String ROCKET_MQ_VIDEO_REPORT_KEY = "video_report";
    public static final String ROCKET_MQ_VIDEO_COLLECT_KEY = "video_collect";
    public static final String ROCKET_MQ_VIDEO_SHARE_KEY = "video_share";
    public static final String ROCKET_MQ_VIDEO_WATCH_KEY = "video_watch";
    public static final String ROCKET_PUBLISH_VIDEO_KEY = "video_publish";
    public static final String ROCKET_MQ_SYNC_VIDEO_DATA_KEY = "sync_video_data";
    public static final String ROCKET_MQ_VIDEO_EVENT_HANDLE_KEY = "video_event_handle";
    /**
     * rocket mq 用户
     */
    public static final String ROCKET_MQ_USER_TOPIC = "xishi_user";
    public static final String ROCKET_MQ_USER_PROVIDER = "GID_xishi_user";
    public static final String ROCKET_MQ_USER_CONSUMER = "GID_xishi_user";
    public static final String ROCKET_MQ_USER_TAG = "user_tag";
    public static final String ROCKET_MQ_USER_INIT_KEY = "user_init";
    public static final String ROCKET_MQ_USER_REALNAME_KEY = "user_realname";
    public static final String ROCKET_MQ_USER_GETGOODSTASK_KEY = "user_getgoodstask_realname";
    public static final String ROCKET_MQ_USER_INVITE_KEY = "user_invite";
    public static final String ROCKET_MQ_USER_ATTENTION_KEY = "user_attention";
    public static final String ROCKET_MQ_USER_SEND_GIFT_KEY = "user_send_gift";
    public static final String ROCKET_MQ_USER_NQ_UPPER_LIMIT_RESET_KEY = "user_nq_upper_limit_reset";
    public static final String ROCKET_MQ_USER_CHECK_VIP_KEY = "user_check_vip";
    /**
     * rocket mq 短视频推荐
     */
    public static final String ROCKET_MQ_VIDEO_PRE_TOPIC = "douniu_video_preLoad_pre";
    public static final String ROCKET_MQ_VIDEO_PRE_PROVIDER = "PID_douniu_video_preLoad_pre";
    public static final String ROCKET_MQ_VIDEO_PRE_CONSUMER = "CID_douniu_video_preLoad_pre";
    public static final String ROCKET_MQ_VIDEO_PRE_TAG = "video_preLoad_tag";
    public static final String ROCKET_MQ_VIDEO_PRE_USER_KEY = "video_preLoad";

    /**
     * rocket mq 直播
     */
    public static final String ROCKET_MQ_LIVE_TOPIC = "douniu_live";
    public static final String ROCKET_MQ_LIVE_PROVIDER = "GID_douniu_live";
    public static final String ROCKET_MQ_LIVE_CONSUMER = "GID_douniu_live";
    public static final String ROCKET_MQ_LIVE_TAG = "live_tag";
    public static final String ROCKET_MQ_LIVE_NOTIFY_KEY = "live_notify";
    public static final String ROCKET_MQ_LIVE_COME_OUT_KEY = "live_come_out";
    public static final String ROCKET_MQ_LIVE_PORN_KEY = "live_porn";
    public static final String ROCKET_MQ_LIVE_FLUSH_DATA_KEY = "flush_live_data";


    /**
     * rocket mq 牛角
     */
    public static final String ROCKET_MQ_NIUJIAO_TOPIC = "douniu_niujiao";
    public static final String ROCKET_MQ_NIUJIAO_PROVIDER = "GID_douniu_niujiao";
    public static final String ROCKET_MQ_NIUJIAO_CONSUMER = "GID_douniu_niujiao";
    public static final String ROCKET_MQ_NIUJIAO_TAG = "niujiao_tag";
    public static final String ROCKET_MQ_NIUJIAO_UNPAID_ORDER_CANCEL_KEY = "niujiao_unpaid_order_cancel";
    public static final String ROCKET_MQ_NIUJIAO_RECEIVE_KEY = "niujiao_receive";

    /**
     * rocket mq 短视频索引相关
     */
    public static final String ROCKET_MQ_VIDEO_INDEX_TOPIC = "douniu_video_index";
    public static final String ROCKET_MQ_VIDEO_INDEX_GROUP = "GID_douniu_video_index";
    public static final String ROCKET_MQ_VIDEO_INDEX_TAG = "video_index_tag";
    public static final String ROCKET_MQ_VIDEO_INDEX_KEY = "video_index";
    public static final String ROCKET_MQ_VIDEO_WATCH_RECORD_INDEX_KEY = "video_watch_record_index";

    /**
     * nq  每日任务序号
     */
    public static final Integer NQ_MISSION_TYPE1 = 1;//首次注册
    public static final Integer NQ_MISSION_TYPE2 = 2;//邀请好友
    public static final Integer NQ_MISSION_TYPE3 = 3;//每日签到
    public static final Integer NQ_MISSION_TYPE4 = 4;//点赞视频
    public static final Integer NQ_MISSION_TYPE5 = 5;//评论视频
    public static final Integer NQ_MISSION_TYPE6 = 6;//分享视频
    public static final Integer NQ_MISSION_TYPE7 = 7;//观看视频
    public static final Integer NQ_MISSION_TYPE8 = 8;//发布视频
    public static final Integer NQ_MISSION_TYPE9 = 9;//视频被点赞

    /**
     * 牛角转余额 比例设置
     */
    public static final BigDecimal PRICE_NOR = BigDecimal.valueOf(0.100);
    public static final BigDecimal PRICE_VIP = BigDecimal.valueOf(0.200);

    /**
     * 公告已读未读键值
     */
//    public static final String ANNOUNCEMENT = "DOUNIU_ANNOUNCEMENT_READ";
    public static final String ANNOUNCEMENT_POINT = "DOUNIU_ANN_READ_POINT";
    public static final String ANNOUNCEMENT_NOT_POINT = "DOUNIU_ANN_READ_NOT_POINT";

    /**
     *
     */
    public static final String PAYACCOUNT = "DOUNIU_PAY_ACCOUNT_RECORD";


    /**
     * 日/周上限任务牛气值
     */
//    public static final BigDecimal DAYLIMIT = BigDecimal.valueOf(1000);
//    public static final BigDecimal WEEKLIMIT = BigDecimal.valueOf(1000);
    public static final String NQLIMIT = "douniu_niu_nq_limit";

    /**
     * 签到规则
     */
    public static final String SIGNRULE = "sign_rule_description";

    /**
     * 最新牛角交易通知
     */
    public static final String NEW_NJ_TRADE_NOTITY = "new_nj_trade_notify";
    /**
     * 今日最新牛角价格
     */
    public static final String TODAY_NJ_PRICE = "today_nj_price";
    /**
     * 最后一笔牛角交易价格
     */
    public static final String LAST_NJ_PRICE = "last_nj_price";
    /**
     * 查询牛角商品状态
     */
    public static final String NJ_GOODS_STATE = "njGoods:state:";

    /**
     * 获取评论列表锁
     */
    public static final String VIDEO_COMMENT_LIST_LOCK = "dn_video_commentlist_lock_";
    /**
     * 艺人申请锁
     */
    public static final String LOCK_MZ_APPLAY = "LOCK_MZ_APPLAY_";
    /**
     * 转入牛角锁
     */
    public static final String BUY_NJ_LOCK = "buy_nj_lock_";
    /**
     * 转出牛角锁
     */
    public static final String SELL_NJ_LOCK = "_nj_lock_";
    /**
     * IM token key
     */
    public static final String IM_TOKEN_PREFIX = "im_token_prefix_";
    /**
     * IM token expire time
     */
    public static final long IM_TOKEN_EXPIRE_TIME = 180 * 24 * 60 * 60L;
    /**
     * 参数加密秘钥 16位
     */
    public static final String PARAM_KEY = "dnnhnh2018AesKey";
    /**
     * 公共返回参数秘钥 16位
     */
    public static final String COMMON_PARAMS_SECRET = "smHBhB2018AesKey";
    /**
     * 设置/找回密码操作
     */
    public static final String SMS_FIND_FIND_SET_PASS_KEY = "dn_sm_user_find_set_pwd_";
    /**
     * 支付密码验证redis key
     */
    public static final String PAY_PWD_REDIS_KEY_PREFIX_ = "dn_pay_pwd_prefix_";
    /**
     * 支付密码验证redis 过期时间 10s
     */
    public static final Long PAY_PWD_REDIS_TIME = 10L;
    /**
     * 通用操作
     */
    public static final String SMS_PUBLIC_KEY = "dn_sms_public_key_";

    /**
     * 登录TOKEN缓存前缀
     */
    public static final String LOGIN_TOKEN_PRE = "login_token_";

    /**
     * 登录验证
     */
    public static final String SMS_LOGIN_KEY = "dn_sms_login_key_";
    /**
     * 支付密码前缀
     */
    public static final String PAY_PWD_INPUT = "dn_pay_pwd_input_";
    /**
     * 腾讯云通讯用户前缀
     */
    public static final String USER_SIGN = "dn_new_user_pro_";
    /**
     * 短视频签名算法类型
     */
    public static final String MAC_NAME_SHA1 = "HmacSHA1";
    public static final String SHA1 = "SHA1";
    public static final String MAC_NAME_SHA256 = "HmacSHA256";

    /**
     * 后台视频更新请求
     */
    public static final String ADMIN_VIDEO_REQ_STR = "dnnhnhAdminReqStr";

    /**
     * 分享参数加密
     */
    public static final String SHARE_OTHER_PARAM_KEY = "dnnhnhShareOther";
    public static final String SHARE_USER_PARAM_KEY = "dnnhnhShareUsers";

    /**
     * 后台注册请求加密值;
     */
    public static final String ADMIN_REG_VALUE = "adminRegValue";

    /**
     * 提现锁
     */
    public static final String WITHDRAW_LOCK = "withdraw_lock_";
    /**
     * 支付牛角提升短视频排名
     */
    public static final String IMPROVE_VIDEO_RANK = "improve_video_rank_";
    /**
     * 实名认证锁
     */
    public static final String USER_AUTH_LOCK = "user_auth_lock_";

    /**
     * 实名认证缓存
     */
    public static final String USER_AUTH_CACHE = "user:auth:cache:";
    /**
     * 实名认证活体检测缓存
     */
    public static final String USER_AUTH_RPBIOONLY_CACHE = "user:auth:rpo:cache:";


    /* -----------IM 互动消息类型--------- */
    public static final int IMCMD_PAILN_TEXT = 1;   // 文本消息
    public static final int IMCMD_ENTER_LIVE = 2;   // 用户加入直播 用
    public static final int IMCMD_EXIT_LIVE = 3;   // 用户退出直播 用
    public static final int IMCMD_PRAISE = 4;   // 点赞消息
    public static final int IMCMD_DANMU = 5;   // 弹幕消息
    public static final int IMCMD_ATTENTION = 6;    //关注主播
    public static final int IMCMD_SYSTEMWARNING = 7;   //系统提示相关
    public static final int IMCMD_GIFT = 8;    //礼物消息
    public static final int IMCMD_GIFT_BIG = 9;    //大礼物
    public static final int IMCMD_ANNOUNCE = 10;   //主播公告
    public static final int IMCMD_SYSTEM_CLOSE_LIVE = 11; //系统关闭直播
    public static final int IMCMD_ENTER_BY_DISTANCE_LIVE = 12; //从附近进入直播间
    public static final int IMCMD_REFRESH_LIVE_INFO = 13; //刷新直播间信息 用
    public static final int IMCMD_ATTACHMENT_ANCHOR = 14; //主播被封停 用
    public static final int IMCMD_USER_GAG = 15; //用户被禁言 用
    public static final int IMCMD_USER_GAG_CANCEL = 16; //用户被取消禁言 用
    public static final int IMCMD_USER_HOUSEKEEP = 17; //用户被设置为房管 用
    public static final int IMCMD_USER_HOUSEKEEP_CANCEL = 18; //用户被取消房管 用
    public static final int IMCMD_USER_KICKEDOUT = 19; //用户被踢出 用

    public static final int IMCMD_PUSHER_PUSH_URL = 20; //连麦用户推流地址 用
    public static final int IMCMD_PLAY_PUSHER_PLAY_URL = 21; //其他连麦者播放流 用
    public static final int IMCMD_PUSHER_DEL = 22; //连麦者被关闭; 用

    public static final String SMS_CODE_EFFECT_STR = "10分钟";

    public static final String AVATAR_PATH="/img/avatar";

    public static final String QRCODE_PATH="/img/qrcode";

    public static String level(Integer level) {
        switch (level) {

            case 1:

                return "Mo主";
            case 2:

                return "艺人Mo主";
            case 3:

                return "人气Mo主";
            case 4:

                return "明星Mo主";

            default:

                return "";
        }
    }

}
