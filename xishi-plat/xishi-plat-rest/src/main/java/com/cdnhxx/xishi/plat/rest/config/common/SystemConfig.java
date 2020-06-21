package com.cdnhxx.xishi.plat.rest.config.common;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * system parameter config
 */
@Component
@PropertySource(value = "classpath:config/conf.properties", encoding = "UTF-8")
@Data
public class SystemConfig {

    /**
     * iM key
     */
    @Value(value = "${im.key}")
    private String imkey;
    /**
     * iM Secret
     */
    @Value(value = "${im.secret}")
    private String imSecret;
    /**
     * iM prefix
     */
    @Value(value = "${im.prefix}")
    private String imPrefix;

    /**
     * video api secret
     */
    @Value(value = "${video.secret.id}")
    private String videoApiSecretId;
    /**
     * video api key
     */
    @Value(value = "${video.secret.key}")
    private String videoApiSecretKey;
    /**
     * video token expire time
     */
    @Value(value = "${video.secret.expireTime}")
    private Long videoSecretExpireTime;
    /**
     * video TX domain
     */
    @Value(value = "${video.tx.domain}")
    private String videoTxDomain;
    /**
     * video My domain
     */
    @Value(value = "${video.my.domain}")
    private String videoMyDomain;
    /**
     * sms prefix
     */
    @Value(value = "${sm.sms.prefix}")
    private String smsPrefix;
    /**
     * sms expire time
     */
    @Value(value = "${sm.sms.expireTime}")
    private Long smsExpireTime;
    /**
     * sms app key
     */
    @Value(value = "${sm.sms.appKey}")
    private String smsAppKey;
    /**
     * sms app secret
     */
    @Value(value = "${sm.sms.appSecret}")
    private String smsAppSecret;
    /**
     * sms temp code
     */
    @Value(value = "${sm.sms.tempCode}")
    private String smsTempCode;
    /**
     * sms sign name
     */
    @Value(value = "${sm.sms.signName}")
    private String smsSignName;
    /**
     * sms region
     */
    @Value(value = "${sm.sms.region}")
    private String smsRegion;

    /**
     * default avatar
     */
    @Value(value = "${s.defaultAvatar}")
    private String defaultAvatar;

    /**
     * allow execute ip -- spring job
     */
    @Value(value = "${s.allowTaskIP}")
    private String allowTaskIP;

    /**
     * 高德地图key
     */
    @Value(value = "${map.gd.key}")
    private String gdKey;
    /**
     * 高德地图secret
     */
    @Value(value = "${map.gd.secret}")
    private String gdSecret;
    /**
     * 快递 customerId
     */
    @Value(value = "${kd.customerId}")
    private String kdCustomerId;
    /**
     * 快递 key
     */
    @Value(value = "${kd.customerKey}")
    private String kdCustomerKey;
    /**
     * 阿里云 rocket mq key
     */
    @Value(value = "${aliyun.rocketmq.AccessKey}")
    private String rocketMqKey;

    /**
     * 阿里云 rocket mq secret
     */
    @Value(value = "${aliyun.rocketmq.SecretKey}")
    private String rocketMqSecret;
    /**
     * 阿里云 rocket mq 服务切入点
     */
    @Value(value = "${aliyun.rocketmq.ONSAddr}")
    private String rocketMqOnsAddr;


    /** 直播点播相关配置**/

    /**
     * 业务ID
     */
    @Value(value = "${dx.live.bizid}")
    private String liveBizid;
    /**
     * 推流域名
     */
    @Value(value = "${dx.live.pushDomain}")
    private String livePushDomain;
    /**
     * 播流域名
     */
    @Value(value = "${dx.live.playDomain}")
    private String livePlayDomain;
    /**
     * 推流防盗链Key
     */
    @Value(value = "${dx.live.pushGuardKey}")
    private String livePushGuardKey;
    /**
     * api鉴权key
     */
    @Value(value = "${dx.live.apiAuthKey}")
    private String liveApiAuthKey;
    /**
     * appid
     */
    @Value(value = "${dx.live.appId}")
    private String liveAppId;

    /**
     * 鉴黄回到鉴权id
     */
    @Value(value = "${dx.screen.id}")
    private String screenId;
    /**
     * 鉴黄回到鉴权key
     */
    @Value(value = "${dx.screen.key}")
    private String screenKey;
    /**
     * 云API密匙ID
     */
    @Value(value = "${dx.live.yunApiSecretId}")
    private String liveYunApiSecretId;
    /**
     * 云API密匙key
     */
    @Value(value = "${dx.live.yunApiSecretKey}")
    private String liveYunApiSecretKey;
    /**
     * 云通信appsdkid
     */
    @Value(value = "${dx.live.sdkAppId}")
    private String liveYunSDKAppId;
    /**
     * 云通信appsdkname
     */
    @Value(value = "${dx.live.adminName}")
    private String liveYunAdminName;
    /**
     * 直播心跳定时任务允许执行的服务器IP
     */
    @Value(value = "${dx.live.heart.allow.ip}")
    private String liveHeartAllowIP;
    /**
     * 云通信公钥
     */
    @Value(value = "${dx.live.pubStr}")
    private String liveYunPubStr;
    /**
     * 云通信user_sign 前缀
     */
    @Value(value = "${dx.live.userSign.prefix}")
    private String liveYunUserSignPrefix;
    /**
     * 云通信私钥
     */
    @Value(value = "${dx.live.priStr}")
    private String liveYunPriStr;

    /**
     * 视频相关请求基路径
     */
    @Value("${dx.video.request.base.url}")
    private String videoBaseUrl;

    /**
     * 视频分类结构接口名称
     */
    @Value("${dx.video.category.construct}")
    private String videoCategoryConstruct;

    /**
     * 视频分类信息接口名称;
     */
    @Value("${dx.video.category.info}")
    private String videoCategoryInfo;

    /**
     * 用户防沉迷保护过期时间
     */
    @Value(value = "${dx.user.protect.expireTime}")
    private Long userProtectExpireTime;


    /**
     * 腾讯视频播放域名
     */
    @Value("${dx.video.tx.domain}")
    private String txDomain;

    /**
     * 自定义视频播放域名
     */
    @Value("${dx.video.my.domain}")
    private String myDomain;

    /**
     * 接收邮件账户;
     */
    @Value("${dx.mail.receive}")
    private String mailAccount;

    /**
     * 用户任务运行机器IP
     */
    @Value(value = "${s.user}")
    private String userExecuteIP;
    /**
     * 视频任务运行机器IP
     */
    @Value(value = "${s.video}")
    private String videoExecuteIP;

}
