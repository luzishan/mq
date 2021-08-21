package com.snym.backup_exchange;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
public class BackupExchangeProducer {

    public static final String B_CONFIRM_EXCHANGE = "b.confirm.exchange";

    @Autowired
    private RabbitTemplate rabbitTemplate;


    @GetMapping("sendBackupMsg/{msg}")
    public void sendMsg(@PathVariable("msg") String msg){
        //指定消息id
        CorrelationData correlationData1 = new CorrelationData(UUID.randomUUID().toString());
        String routingKey = "key1";
        String msg1 = msg+"-"+routingKey;
        rabbitTemplate.convertAndSend(B_CONFIRM_EXCHANGE,routingKey,msg1,correlationData1);
        log.info("发送消息id：{}，内容为：{}",correlationData1.getId(),msg1);

        CorrelationData correlationData2 = new CorrelationData(UUID.randomUUID().toString());
        routingKey = "key2";
        String msg2 = msg+"-"+routingKey;
        rabbitTemplate.convertAndSend(B_CONFIRM_EXCHANGE,routingKey,msg+"-"+routingKey,correlationData2);
        log.info("发送消息id：{}，内容为：{}",correlationData2.getId(),msg2);
    }

}
