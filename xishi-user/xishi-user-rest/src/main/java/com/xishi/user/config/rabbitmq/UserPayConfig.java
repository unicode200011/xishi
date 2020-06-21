package com.xishi.user.config.rabbitmq;

import com.xishi.user.entity.Enum.MqQueueNameEnum;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserPayConfig {

    /**
     * 用户相关队列
     * @return
     */
    //队列
    @Bean
    public Queue UserPayQueue() {
        return new Queue(MqQueueNameEnum.UserPayRabbit.getQueueName(),true);  //true 是否持久
    }

    //Direct交换机
    @Bean
    DirectExchange UserPayExchange() {
        return new DirectExchange(MqQueueNameEnum.UserPayRabbit.getExchangeName());
    }

    //绑定
    @Bean
    Binding bindingUserPayDirect() {
        return BindingBuilder.bind(UserPayQueue()).to(UserPayExchange()).with(MqQueueNameEnum.UserPayRabbit.getRouteName());
    }

}
