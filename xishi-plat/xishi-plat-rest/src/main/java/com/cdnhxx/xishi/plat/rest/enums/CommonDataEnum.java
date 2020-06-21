package com.cdnhxx.xishi.plat.rest.enums;

public enum CommonDataEnum {

    USER_XY(1, "userInfo", "用户协议"),
    LIVEXY(2, "liveXy", "直播运营"),
    ABOUTUS(3, "aboutUs", "关于我们"),
    NOTICE(4, "Notice", "直播间公告"),
    AREA(5, "area", "社区公约"),
    PRIVET(6, "privet", "隐私政策"),
    ALIVE(7, "alive", "主播协议"),
    SIGN(8, "sign", "签约说明"),
    AD(9, "ad", "APP广告投放"),
    LIVE_AD(10, "live_ad", "直播间广告"),
    CHARGE(11, "charge", "充值咨询"),
    OFFICE(12, "office", "联系官方"),
    SHARE(15, "share", "推广合作"),
    shower(16, "shower", "主播用户协议"),


    IOS_MIN(21, "iosMin", "ios最低版本"),
    IOS_MAX(22, "iosMax", "ios最高版本"),
    IOS_UPDATE_DESC(23, "iosUpdateDesc", "iOS更新描述"),
    IOS_DOWN_URL(24, "iosDownUrl", "iOS下载链接"),
    AND_MIN(25, "andMin", "安卓最低版本"),
    AND_MAX(26, "andMax", "安卓最高版本"),
    AND_UPDATE_DESC(27, "andUpdateDesc", "安卓更新描述"),
    AND_DOWN_URL(28, "andDownUrl", "安卓下载链接"),
    ;

    private Integer num;

    private String key;

    private String name;

    private CommonDataEnum(Integer num, String key, String name) {
        this.num = num;
        this.key = key;
        this.name = name;
    }

    public Integer getNum() {
        return num;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public static CommonDataEnum getDateEnumByNum(Integer num) {
        if (num == null) {
            return null;
        }
        for (CommonDataEnum per : CommonDataEnum.values()) {
            if (per.getNum().intValue() == num) {
                return per;
            }
        }
        return null;
    }
}
