package com.xishi.socket.mq;

import com.alibaba.fastjson.JSONObject;
import com.xishi.socket.entity.Enum.MqUserServiceTypeConstens;
import com.xishi.user.entity.mqMessage.LivePayRabbitMessage;
import com.xishi.user.entity.mqMessage.LiveRabbitMessage;
import com.xishi.user.entity.mqMessage.MoviePayRabbitMessage;
import com.xishi.user.entity.mqMessage.UserRabbitMessage;
import com.xishi.user.entity.Enum.MqQueueNameEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MqSender {

    @Autowired
    RabbitTemplate rabbitTemplate;  //使用RabbitTemplate,这提供了接收/发送等等方法

    /**
     * 用户相关-断开连接 消息
     * @param message
     * @return
     */
    public boolean sendUserLogoutMessage(UserRabbitMessage message){
        log.info("【MqSender】发送用户断开连接消息");
        //将消息携带绑定键值：TestDirectRouting 发送到交换机TestDirectExchange
        message.setType(MqUserServiceTypeConstens.USER_LOGOUT);
        rabbitTemplate.convertAndSend(MqQueueNameEnum.UserRabbit.getExchangeName(), MqQueueNameEnum.UserRabbit.getRouteName(), JSONObject.toJSONString(message));
        return true;
    }

    /**
     * 用户相关-上线连接 消息
     * @param message
     * @return
     */
    public boolean sendUserLoginMessage(UserRabbitMessage message){
        log.info("【MqSender】发送用户上线连接消息");
        //将消息携带绑定键值：TestDirectRouting 发送到交换机TestDirectExchange
        message.setType(MqUserServiceTypeConstens.USER_LOGIN);
        rabbitTemplate.convertAndSend(MqQueueNameEnum.UserRabbit.getExchangeName(), MqQueueNameEnum.UserRabbit.getRouteName(), JSONObject.toJSONString(message));
        return true;
    }
    /**
     * 扣除计时收费 消息
     * @param message
     * @return
     */
    public boolean sendLivePayMessage(LivePayRabbitMessage message){
        //将消息携带绑定键值：TestDirectRouting 发送到交换机TestDirectExchange
        log.info("发送扣除计时收费消息");
        message.setLiveMode(1);
        rabbitTemplate.convertAndSend(MqQueueNameEnum.UserPayRabbit.getExchangeName(), MqQueueNameEnum.UserPayRabbit.getRouteName(), JSONObject.toJSONString(message));
        return true;
    }

    /**
     * 更新计时收费 消费记录消息
     * @param message
     * @return
     */
    public boolean sendLivePayRecordMessage(LivePayRabbitMessage message) {
        //将消息携带绑定键值：TestDirectRouting 发送到交换机TestDirectExchange
        log.info("发送更新计时收费 消费记录消息");
        message.setLiveMode(2);
        rabbitTemplate.convertAndSend(MqQueueNameEnum.UserPayRabbit.getExchangeName(), MqQueueNameEnum.UserPayRabbit.getRouteName(), JSONObject.toJSONString(message));
        return true;
    }
    /**
     * 发送直播结束消息
     * @param message
     * @return
     */
    public boolean sendEndLiveMessage(LiveRabbitMessage message) {
        //将消息携带绑定键值：TestDirectRouting 发送到交换机TestDirectExchange
        log.info("发送更新计时收费 消费记录消息");
        rabbitTemplate.convertAndSend(MqQueueNameEnum.LiveShowRabbit.getExchangeName(), MqQueueNameEnum.LiveShowRabbit.getRouteName(), JSONObject.toJSONString(message));
        return true;
    }

    /**
     * 看电影收费 消息
     * @param message
     * @return
     */
    public boolean sendMoviePayMessage(MoviePayRabbitMessage message){
        //将消息携带绑定键值：TestDirectRouting 发送到交换机TestDirectExchange
        log.info("发送看电影收费消息");
        rabbitTemplate.convertAndSend(MqQueueNameEnum.MoviePayRabbit.getExchangeName(), MqQueueNameEnum.MoviePayRabbit.getRouteName(), JSONObject.toJSONString(message));
        return true;
    }

}
