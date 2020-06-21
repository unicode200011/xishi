package com.xishi.liveShow.mq;

import com.alibaba.fastjson.JSONObject;
import com.xishi.user.entity.Enum.MqQueueNameEnum;
import com.xishi.user.entity.mqMessage.LiveGiftRabbitMessage;
import com.xishi.user.entity.mqMessage.LivePayRabbitMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MqLiveShowSender {

    @Autowired
    RabbitTemplate rabbitTemplate;  //使用RabbitTemplate,这提供了接收/发送等等方法

    /**
     * 直播相关 消息
     * @param message
     * @return
     */
    public boolean sendLiveShowMessage(Object message){
        //将消息携带绑定键值：TestDirectRouting 发送到交换机TestDirectExchange
        rabbitTemplate.convertAndSend(MqQueueNameEnum.LiveShowRabbit.getExchangeName(), MqQueueNameEnum.LiveShowRabbit.getRouteName(), JSONObject.toJSONString(message));
        return true;
    }

    /**
     * 赠送礼物 消息
     * @param message
     * @return
     */
    public boolean sendLiveGiftMessage(LiveGiftRabbitMessage message){
        //将消息携带绑定键值：TestDirectRouting 发送到交换机TestDirectExchange
        log.info("发送赠送礼物消息");
        rabbitTemplate.convertAndSend(MqQueueNameEnum.LiveGiftRabbit.getExchangeName(), MqQueueNameEnum.LiveGiftRabbit.getRouteName(), JSONObject.toJSONString(message));
        return true;
    }
    /**
     * 扣除常规模式门票 消息
     * @param message
     * @return
     */
    public boolean sendLivePayMessage(LivePayRabbitMessage message){
        //将消息携带绑定键值：TestDirectRouting 发送到交换机TestDirectExchange
        log.info("发送扣除收费模式消息");
        rabbitTemplate.convertAndSend(MqQueueNameEnum.UserPayRabbit.getExchangeName(), MqQueueNameEnum.UserPayRabbit.getRouteName(), JSONObject.toJSONString(message));
        return true;
    }

}
