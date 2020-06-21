package com.xishi.user.entity.Enum;

import lombok.Getter;

@Getter
public enum MqQueueNameEnum {
    //发布动态
    DynamicSend(0,"dynamic_send_queue","dynamic_send_exchange","dynamic_send_route"),
    //动态点赞,
    DynamicPraise(1,"dynamic_praise_queue","dynamic_praise_exchange","dynamic_praise_route"),
    //动态删除
    DynamicDelete(2,"dynamic_delete_queue","dynamic_delete_exchange","dynamic_delete_route"),
    //直播相关
    LiveShowRabbit(4,"live_show_queue","live_show_exchange","live_show_route"),
    //礼物相关
    LiveGiftRabbit(5,"live_gift_queue","live_gift_exchange","live_gift_route"),
    //用户相关
    UserRabbit(3,"user_queue","user_exchange","user_route"),
    AgentMoneyRabbit(8,"agent_money_queue","agent_money_exchange","agent_money_route"),
    //代理商提成
    AgentRateMoneyRabbit(8,"agent_rate_money_queue","agent_rate_money_exchange","agent_rate_money_route"),
    //平台提成
    AdminRateMoneyRabbit(9,"admin_rate_money_queue","admin_rate_money_exchange","admin_rate_money_route"),
    //看电影收费相关
    MoviePayRabbit(7,"movie_pay_queue","movie_pay_exchange","movie_pay_route"),
    //用户直播支付相关
    UserPayRabbit(6,"user_pay_queue","user_pay_exchange","user_pay_route");

    private Integer code;
    private String queueName;
    private String exchangeName;
    private String routeName;

    MqQueueNameEnum(Integer code,String queueName,String exchangeName,String routeName){
        this.code = code;
        this.queueName = queueName;
        this.exchangeName = exchangeName;
        this.routeName = routeName;
    }
}
