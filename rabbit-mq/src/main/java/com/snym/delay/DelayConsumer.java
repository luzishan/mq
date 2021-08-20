package com.snym.delay;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 延时消费者
 *
 * @author lzs
 * @version 1.0
 * @date 2021/8/20 23:43
 */
@Configuration
@Slf4j
public class DelayConsumer {

   private static final String DELAY_QUEUE = "delayed.queue";

    @RabbitListener(queues = DELAY_QUEUE)
    public void receiveDelayMsg(Message message){
        String msg = new String(message.getBody());
        log.info("当前时间：{}，收到延时队列的消息：{}",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),msg);
    }

}
