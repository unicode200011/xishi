package com.xishi.basic.entity.enums;

public enum CommonDataEnum {

    USER_XY(1,"userXy","用户协议"),
    SERVICE_FEE(2,"userCommission","手续费说明"),
    EXCHANGE_KANDOU(3,"exchangeKdCommission","交换看豆说明"),
    KANDOU_NOTE(4,"kdCommission","看豆说明"),
    KANVALUE_NOTE(5,"kzCommission","看值说明"),
    ACTIVE_NOTE(6,"activeCommission","活跃度说明"),
    TERM_NOTE(7,"activeTeamCommission","团队活跃度说明"),
    SMALLAREA_NOTE(8,"smallAreaCommission","小区活跃度说明"),
    BIGAREA_NOTE(9,"bigAreaCommission","大区活跃度说明"),
    REALNAME_NOTE(10,"kwRealNameCommission","西施实名协议"),
    CONTACT_US(12,"phone","联系我们"),
    KANDOU_PERCENT(13,"kdComputeRule","加成看豆计算规则"),
    ACTIVE_PERCENT(17,"activeComputeRule","加成活跃度计算规则"),
    KANDOU_MAX_PRICE(18,"kdMaxPrice","看豆最高价"),
    KANDOU_MIN_PRICE(19,"kdMinPrice","看豆最低价"),
    SHOW_MARKET(20,"kdExchangeDetail","是否展示看豆交换行情"),
    RMB_RATE(21,"rate","人民币汇率"),
    EXCHANGE_BOTH(22,"exchangeOrderBoth","买卖双方均未操作"),
    EXCHANGE_BUYER(23,"exchangeOrderBuyer","买方已确认付款，卖方未确认收款"),
    EXCHANGE_SELLER(24,"exchangeOrderSeller","卖方已确认收款，买方未确认收款"),
    EXCHANGE_CLOSE(25,"exchangeOrderClose","订单关闭看豆到账时间"),
    TASK_RULE(26,"taskRule","任务规则说明"),
    GRAGE_NOTE(27,"gradeNote","等级说明"),
    PARTNER_NOTE(28,"cityPartner","城市合伙人权益说明"),
    MISS_RULE(29,"missRule","补看规则说明"),
    AND_VERSION(30,"and_version_max","安卓目前最高版本"),
    IOS_VERSION(31,"ios_version_max","ios目前最高版本"),
    IOS_URL(33,"ios_url","ios下载地址"),
    AND_URL(34,"and_url","安卓下载地址"),
    CUSTOMER_TEL(32,"service_phone","客服电话"),
    CUSTOMER_QQ(35,"service_qq","客服qq"),;

    private Integer num;

    private String key;

    private String name;

    private CommonDataEnum(Integer num, String key, String name) {
        this.num=num;
        this.key=key;
        this.name=name;
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
        if(num==null) {
            return null;
        }
        for(CommonDataEnum per:CommonDataEnum.values()) {
            if(per.getNum().intValue()==num) {
                return per;
            }
        }
        return null;
    }
}
