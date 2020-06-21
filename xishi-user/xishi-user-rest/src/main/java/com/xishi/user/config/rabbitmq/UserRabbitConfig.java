package com.xishi.user.config.rabbitmq;

import com.xishi.user.entity.Enum.MqQueueNameEnum;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserRabbitConfig {

    /**
     * 用户相关队列
     * @return
     */
    //队列
    @Bean
    public Queue UserQueue() {
        return new Queue(MqQueueNameEnum.UserRabbit.getQueueName(),true);  //true 是否持久
    }

    //Direct交换机
    @Bean
    DirectExchange UserExchange() {
        return new DirectExchange(MqQueueNameEnum.UserRabbit.getExchangeName());
    }

    //绑定
    @Bean
    Binding bindingUserDirect() {
        return BindingBuilder.bind(UserQueue()).to(UserExchange()).with(MqQueueNameEnum.UserRabbit.getRouteName());
    }


    //队列
    @Bean
    public Queue AgentMoneyQueue() {
        return new Queue(MqQueueNameEnum.AgentRateMoneyRabbit.getQueueName(),true);  //true 是否持久
    }

    //Direct交换机
    @Bean
    DirectExchange AgentMoneyExchange() {
        return new DirectExchange(MqQueueNameEnum.AgentRateMoneyRabbit.getExchangeName());
    }

    //绑定
    @Bean
    Binding bindingAgentMoneyDirect() {
        return BindingBuilder.bind(AgentMoneyQueue()).to(AgentMoneyExchange()).with(MqQueueNameEnum.AgentRateMoneyRabbit.getRouteName());
    }

    //队列
    @Bean
    public Queue AdminMoneyQueue() {
        return new Queue(MqQueueNameEnum.AdminRateMoneyRabbit.getQueueName(),true);  //true 是否持久
    }

    //Direct交换机
    @Bean
    DirectExchange AdminMoneyExchange() {
        return new DirectExchange(MqQueueNameEnum.AdminRateMoneyRabbit.getExchangeName());
    }

    //绑定
    @Bean
    Binding bindingAdminMoneyDirect() {
        return BindingBuilder.bind(AdminMoneyQueue()).to(AdminMoneyExchange()).with(MqQueueNameEnum.AdminRateMoneyRabbit.getRouteName());
    }



    //队列
    @Bean
    public Queue LiveGiftQueue() {
        return new Queue(MqQueueNameEnum.LiveGiftRabbit.getQueueName(),true);  //true 是否持久
    }

    //Direct交换机
    @Bean
    DirectExchange LiveGiftExchange() {
        return new DirectExchange(MqQueueNameEnum.LiveGiftRabbit.getExchangeName());
    }

    //绑定
    @Bean
    Binding bindingLiveGiftDirect() {
        return BindingBuilder.bind(LiveGiftQueue()).to(LiveGiftExchange()).with(MqQueueNameEnum.LiveGiftRabbit.getRouteName());
    }


    //队列
    @Bean
    public Queue LiveShowQueue() {
        return new Queue(MqQueueNameEnum.LiveShowRabbit.getQueueName(),true);  //true 是否持久
    }

    //Direct交换机
    @Bean
    DirectExchange LiveShowExchange() {
        return new DirectExchange(MqQueueNameEnum.LiveShowRabbit.getExchangeName());
    }

    //绑定
    @Bean
    Binding bindingLiveShowDirect() {
        return BindingBuilder.bind(LiveShowQueue()).to(LiveShowExchange()).with(MqQueueNameEnum.LiveShowRabbit.getRouteName());
    }


    //队列
    @Bean
    public Queue MoviePayQueue() {
        return new Queue(MqQueueNameEnum.MoviePayRabbit.getQueueName(),true);  //true 是否持久
    }

    //Direct交换机
    @Bean
    DirectExchange MoviePayExchange() {
        return new DirectExchange(MqQueueNameEnum.MoviePayRabbit.getExchangeName());
    }

    //绑定
    @Bean
    Binding bindingMoviePayDirect() {
        return BindingBuilder.bind(MoviePayQueue()).to(MoviePayExchange()).with(MqQueueNameEnum.MoviePayRabbit.getRouteName());
    }
}
