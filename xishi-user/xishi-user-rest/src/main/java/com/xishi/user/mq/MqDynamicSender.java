package com.xishi.user.mq;

import com.alibaba.fastjson.JSONObject;
import com.xishi.user.entity.Enum.MqQueueNameEnum;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MqDynamicSender {

    @Autowired
    RabbitTemplate rabbitTemplate;  //使用RabbitTemplate,这提供了接收/发送等等方法

    /**
     * 评论点赞 消息
     * @param message
     * @return
     */
    public boolean sendDynamicPraiseMessage(Object message){
        //将消息携带绑定键值：TestDirectRouting 发送到交换机TestDirectExchange
        rabbitTemplate.convertAndSend(MqQueueNameEnum.DynamicPraise.getExchangeName(), MqQueueNameEnum.DynamicPraise.getRouteName(), JSONObject.toJSONString(message));
        return true;
    }
    /**
     * 发布动态
     * @param message
     * @return
     */
    public boolean sendDynamicMessage(Object message){
        //将消息携带绑定键值：TestDirectRouting 发送到交换机TestDirectExchange
        rabbitTemplate.convertAndSend(MqQueueNameEnum.DynamicSend.getExchangeName(), MqQueueNameEnum.DynamicSend.getRouteName(), JSONObject.toJSONString(message));
        return true;
    }

    /**
     * 删除动态
     * @param message
     * @return
     */
    public boolean deleteDynamicMessage(Object message){
        //将消息携带绑定键值：TestDirectRouting 发送到交换机TestDirectExchange
        rabbitTemplate.convertAndSend(MqQueueNameEnum.DynamicDelete.getExchangeName(), MqQueueNameEnum.DynamicDelete.getRouteName(), JSONObject.toJSONString(message));
        return true;
    }

}
