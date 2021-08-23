package com.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 发布确认交换机和队列配置
 *
 * @author lzs
 * @version 1.0
 * @date 2021/8/21 0:56
 */
@Configuration
public class PublishConfirmConfig {

    private static final String CONFIRM_EXCHANGE = "confirm.exchange";

    public static final String CONFIRM_QUEUE = "confirm.queue";

    @Bean("confirmQueue")
    public Queue confirmQueue() {
        return new Queue(CONFIRM_QUEUE);
    }

    @Bean("confirmExchange")
    public DirectExchange confirmExchange() {
        return new DirectExchange(CONFIRM_EXCHANGE);
    }

    @Bean
    public Binding confirmQueueBindingConfirmExchange(@Qualifier("confirmQueue") Queue confirmQueue,
                                                      @Qualifier("confirmExchange") DirectExchange confirmExchange) {
        return BindingBuilder.bind(confirmQueue).to(confirmExchange).with("key1");
    }
}
