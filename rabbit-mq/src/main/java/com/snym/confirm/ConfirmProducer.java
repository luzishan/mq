package com.snym.confirm;

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
@RequestMapping("/confirm")
@Slf4j
public class ConfirmProducer {

    private static final String CONFIRM_EXCHANGE = "confirm.exchange";

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private MessageCallBack messageCallBack;

    //依赖注入rabbitTemplate后设置回调对象
    /*@PostConstruct
    public void init(){
        rabbitTemplate.setConfirmCallback(messageCallBack);
    }*/

    @GetMapping("sendMsg/{msg}")
    public void sendMsg(@PathVariable("msg") String msg){
        //指定消息id
        CorrelationData correlationData1 = new CorrelationData(UUID.randomUUID().toString());
        String routingKey = "key1";
        rabbitTemplate.convertAndSend(CONFIRM_EXCHANGE,routingKey,msg+"-"+routingKey,correlationData1);

        CorrelationData correlationData2 = new CorrelationData(UUID.randomUUID().toString());
        routingKey = "key2";
        rabbitTemplate.convertAndSend(CONFIRM_EXCHANGE,routingKey,msg+"-"+routingKey,correlationData2);
    }

}
