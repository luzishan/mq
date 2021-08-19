package com.snym.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 延时队列相关配置
 *
 * @author lzs
 * @version 1.0
 * @date 2021/8/20 0:59
 */
@Configuration
public class TtlQueueConfig {

    //普通交换机
    private static final String X_EXCHANGE = "X";

    //普通队列
    private static final String QUEUE_A = "QA";

    //普通队列
    private static final String QUEUE_B = "QB";

    //死信交换机
    private static final String Y_EXCHANGE = "Y";

    //死信队列
    private static final String QUEUE_D = "QD";

    @Bean("xExchange")
    public DirectExchange xExchange(){
        return new DirectExchange(X_EXCHANGE);
    }

    @Bean("queueA")
    public Queue queueA(){
        Map<String,Object> params = new HashMap<>();
        //声明当前队列绑定死信交换机
        params.put("x-dead-letter-exchange",Y_EXCHANGE);
        //声明当前队列死信路由key
        params.put("x-dead-letter-routing-key","YD");
        //声明当前队列ttl,50000ms
        params.put("x-message-ttl",50000);
        return QueueBuilder.durable(QUEUE_A).withArguments(params).build();
    }

    //声明队列queueA绑定交换机xExchange
    @Bean
    public Binding queueABindingX(@Qualifier("queueA") Queue queueA,@Qualifier("xExchange") DirectExchange xExchange){
        return BindingBuilder.bind(queueA).to(xExchange).with("XA");
    }

    @Bean("queueB")
    public Queue queueB(){
        Map<String,Object> params = new HashMap<>();
        //声明当前队列绑定的死信交换机
        params.put("x-dead-letter-exchange",Y_EXCHANGE);
        //声明当前队列死信路由key
        params.put("x-dead-letter-routing-key","YD");
        //声明当前队列的ttl,20000ms
        params.put("x-message-ttl",20000);
        return QueueBuilder.durable(QUEUE_B).withArguments(params).build();
    }

    @Bean
    public Binding queueBBindingX(@Qualifier("queueB") Queue queueB,@Qualifier("xExchange") DirectExchange xExchange){
        return BindingBuilder.bind(queueB).to(xExchange).with("XB");
    }



    @Bean("yExchange")
    public DirectExchange yExchange(){
        return new DirectExchange(Y_EXCHANGE);
    }

    @Bean("queueD")
    public Queue queueD(){
        return new Queue(QUEUE_D);
    }

    @Bean
    public Binding queueDBindingY(@Qualifier("queueD") Queue queueD,@Qualifier("yExchange") DirectExchange yExchange){
        return BindingBuilder.bind(queueD).to(yExchange).with("YD");
    }

}
