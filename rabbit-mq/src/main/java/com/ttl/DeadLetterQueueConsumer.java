package com.ttl;



import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 消费者
 *
 * @author lzs
 * @version 1.0
 * @date 2021/8/20 1:33
 */
@Component
@Slf4j
public class DeadLetterQueueConsumer {

    //消费者监听的队列
    @RabbitListener(queues = "QD")
    public void receiveMsg(Message message, Channel channel){
        String msg = new String(message.getBody());
        log.info("当前时间：{}，收到死信队列的消息：{}",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),msg);
    }
}
