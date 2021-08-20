package com.snym.config;

import com.rabbitmq.client.BuiltinExchangeType;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 用交换机控制延迟时间
 *
 * @author lzs
 * @version 1.0
 * @date 2021/8/20 23:26
 */
@Configuration
public class DelayExchangeConfig {

    private static final String DELAYED_EXCHANGE = "delayed.exchange";

    private static final String DELAYED_QUEUE = "delayed.queue";

    private static final String DELAYED_ROUTING_KEY = "delayed.routingkey";

  //定义队列
    @Bean("delayQueue")
    public Queue delayQueue() {
        return new Queue(DELAYED_QUEUE);
    }

    //定义交换机，延时交换机
   @Bean("delayExchange")
    public CustomExchange delayExchange() {
        Map<String, Object> params = new HashMap<>();
        //direct千万不可使用枚举获取！！！，否则连接不上rabbitmq
        params.put("x-delayed-type", "direct");
        return new CustomExchange(DELAYED_EXCHANGE, "x-delayed-message", true, false, params);
    }

    //绑定交换机和队列
  @Bean
    public Binding delayQueueBindingDelayExchange(@Qualifier("delayQueue") Queue delayQueue,
                                                  @Qualifier("delayExchange") CustomExchange delayExchange) {
        return BindingBuilder.bind(delayQueue).to(delayExchange).with(DELAYED_ROUTING_KEY).noargs();
    }


}
