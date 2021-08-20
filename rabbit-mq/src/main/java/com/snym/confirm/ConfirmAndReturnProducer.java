package com.snym.confirm;

import com.snym.callback.ConfirmAndReturnCallBack;
import com.snym.callback.MessageCallBack;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.UUID;

/**
 * 消息确认生产者
 *
 * @author lzs
 * @version 1.0
 * @date 2021/8/21 1:03
 */
@RestController
@RequestMapping("/confirm_return")
@Slf4j
public class ConfirmAndReturnProducer {

    private static final String CONFIRM_EXCHANGE = "confirm.exchange";

    @Autowired
    private RabbitTemplate rabbitTemplate;


    //依赖注入rabbitTemplate后设置回调对象
   /* @PostConstruct
    public void init(){
        //设置回调
        rabbitTemplate.setConfirmCallback(messageCallBack);
    }*/

    //依赖注入rabbitTemplate后设置回调对象
   /* @PostConstruct
    public void init(){
        //设置回调
        rabbitTemplate.setConfirmCallback(confirmAndReturnCallBack);
        //true：交换机无法将消息路由时，将消息返回给生产者
        //false：交换机无法将消息路由时，将消息丢弃
        rabbitTemplate.setMandatory(true);
        //设置回退消息处理
        rabbitTemplate.setReturnCallback(confirmAndReturnCallBack);
    }*/

    @GetMapping("sendMsg/{msg}")
    public void sendMsg(@PathVariable("msg") String msg){
        //指定消息id
        CorrelationData correlationData1 = new CorrelationData(UUID.randomUUID().toString());
        String routingKey = "key1";
        String msg1 = msg+"-"+routingKey;
        rabbitTemplate.convertAndSend(CONFIRM_EXCHANGE,routingKey,msg1,correlationData1);
        log.info("发送消息id：{}，内容为：{}",correlationData1.getId(),msg1);

        CorrelationData correlationData2 = new CorrelationData(UUID.randomUUID().toString());
        routingKey = "key2";
        String msg2 = msg+"-"+routingKey;
        rabbitTemplate.convertAndSend(CONFIRM_EXCHANGE,routingKey,msg+"-"+routingKey,correlationData2);
        log.info("发送消息id：{}，内容为：{}",correlationData2.getId(),msg2);
    }

}
