package com.delay;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 延时消息生产者
 *
 * @author lzs
 * @version 1.0
 * @date 2021/8/20 23:38
 */
@RestController
@RequestMapping("/delay")
@Slf4j
public class DelayProducer {

    private static final String DELAY_EXCHANGE = "delayed.exchange";

    private static final String DELAY_ROUTING_KEY = "delayed.routingkey";

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("sendMsg/{msg}/{delayTime}")
    public void sendMsg(@PathVariable("msg") String msg, @PathVariable("delayTime") Integer delayTime){
        rabbitTemplate.convertAndSend(DELAY_EXCHANGE,DELAY_ROUTING_KEY,msg,correlationData->{
            correlationData.getMessageProperties().setDelay(delayTime);
            return correlationData;
        });
        log.info("当前时间：{}，发送一条时长：{}毫秒TTL消息给队列delay.queue队列：{}",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),delayTime,msg);
    }
}
