package com.snym.ttl;

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
 * 生产者
 *
 * @author lzs
 * @version 1.0
 * @date 2021/8/20 1:26
 */
@RestController
@RequestMapping("/ttl")
@Slf4j
public class SendMsgController {

    private static final String X_EXCHANGE = "X";

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("sendMsg/{msg}")
    public String sendMsg(@PathVariable("msg") String msg){
        log.info("当前时间：{}，发送一条消息给两个ttl队列：{}",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),msg);
        rabbitTemplate.convertAndSend(X_EXCHANGE,"XA",msg);
        rabbitTemplate.convertAndSend(X_EXCHANGE,"XB",msg);
        return "消息："+msg+"发送成功";
    }
}
