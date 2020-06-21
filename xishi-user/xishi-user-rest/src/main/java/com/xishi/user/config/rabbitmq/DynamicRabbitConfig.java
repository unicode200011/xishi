package com.xishi.user.config.rabbitmq;

import com.xishi.user.entity.Enum.MqQueueNameEnum;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DynamicRabbitConfig {

    /**
     * 点赞
     * @return
     */
    //队列
    @Bean
    public Queue DynamicPraiseQueue() {
        return new Queue(MqQueueNameEnum.DynamicPraise.getQueueName(),true);  //true 是否持久
    }

    //Direct交换机
    @Bean
    DirectExchange DynamicPraiseExchange() {
        return new DirectExchange(MqQueueNameEnum.DynamicPraise.getExchangeName());
    }

    //绑定
    @Bean
    Binding bindingDynamicPraiseDirect() {
        return BindingBuilder.bind(DynamicPraiseQueue()).to(DynamicPraiseExchange()).with(MqQueueNameEnum.DynamicPraise.getRouteName());
    }

    /**
     * 发布动态
     * @return
     */
    @Bean
    public Queue DynamicSendQueue() {
        return new Queue(MqQueueNameEnum.DynamicSend.getQueueName(),true);  //true 是否持久
    }

    @Bean
    DirectExchange DynamicSendExchange() {
        return new DirectExchange(MqQueueNameEnum.DynamicSend.getExchangeName());
    }

    @Bean
    Binding bindingDynamicSendDirect() {
        return BindingBuilder.bind(DynamicSendQueue()).to(DynamicSendExchange()).with(MqQueueNameEnum.DynamicSend.getRouteName());
    }
    /**
     * 删除动态
     * @return
     */
    @Bean
    public Queue DynamicDeleteQueue() {
        return new Queue(MqQueueNameEnum.DynamicDelete.getQueueName(),true);  //true 是否持久
    }

    @Bean
    DirectExchange DynamicDeleteExchange() {
        return new DirectExchange(MqQueueNameEnum.DynamicDelete.getExchangeName());
    }

    @Bean
    Binding bindingDynamicDeleteDirect() {
        return BindingBuilder.bind(DynamicDeleteQueue()).to(DynamicDeleteExchange()).with(MqQueueNameEnum.DynamicDelete.getRouteName());
    }

}
