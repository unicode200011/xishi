package com.xishi.user.mq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xishi.user.entity.Enum.MqQueueNameEnum;
import com.xishi.user.entity.mqMessage.AdminMoneyRabbitMessage;
import com.xishi.user.entity.mqMessage.AgentMoneyRabbitMessage;
import com.xishi.user.entity.mqMessage.UserRabbitMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MqUserSender {

    @Autowired
    RabbitTemplate rabbitTemplate;  //使用RabbitTemplate,这提供了接收/发送等等方法

    /**
     * 用户等级 消息
     * @param message
     * @return
     */
    public boolean sendAddUserGradeMessage(UserRabbitMessage message){
        log.info("发送用户等级MQ消息，data【{}】", JSON.toJSONString(message));
        //将消息携带绑定键值：TestDirectRouting 发送到交换机TestDirectExchange
        rabbitTemplate.convertAndSend(MqQueueNameEnum.UserRabbit.getExchangeName(), MqQueueNameEnum.UserRabbit.getRouteName(), JSONObject.toJSONString(message));
        return true;
    }
    /**
     * 直播等级 消息
     * @param message
     * @return
     */
    public boolean sendAddLiveGradeMessage(UserRabbitMessage message){
        //将消息携带绑定键值：TestDirectRouting 发送到交换机TestDirectExchange
        log.info("发送直播等级MQ消息，data【{}】", JSON.toJSONString(message));
        rabbitTemplate.convertAndSend(MqQueueNameEnum.UserRabbit.getExchangeName(), MqQueueNameEnum.UserRabbit.getRouteName(), JSONObject.toJSONString(message));
        return true;
    }
    /**
     * 代理商提成 消息
     * @param message
     * @return
     */
    public boolean sendAddAgentMoneyMessage(AgentMoneyRabbitMessage message){
        //将消息携带绑定键值：TestDirectRouting 发送到交换机TestDirectExchange
        log.info("发送代理商提成MQ消息，data【{}】", JSON.toJSONString(message));
        rabbitTemplate.convertAndSend(MqQueueNameEnum.AgentRateMoneyRabbit.getExchangeName(), MqQueueNameEnum.AgentRateMoneyRabbit.getRouteName(), JSONObject.toJSONString(message));
        return true;
    }

    /**
     * 平台提成 消息
     * @param message
     * @return
     */
    public boolean sendAddAdminMoneyMessage(AdminMoneyRabbitMessage message){
        //将消息携带绑定键值：TestDirectRouting 发送到交换机TestDirectExchange
        log.info("发送平台提成MQ消息，data【{}】", JSON.toJSONString(message));
        rabbitTemplate.convertAndSend(MqQueueNameEnum.AdminRateMoneyRabbit.getExchangeName(), MqQueueNameEnum.AdminRateMoneyRabbit.getRouteName(), JSONObject.toJSONString(message));
        return true;
    }

}
