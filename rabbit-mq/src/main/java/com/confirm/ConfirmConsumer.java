package com.confirm;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 消息消费者
 *
 * @author lzs
 * @version 1.0
 * @date 2021/8/21 1:18
 */
@Component
@Slf4j
public class ConfirmConsumer {

    public static final String CONFIRM_QUEUE = "confirm.queue";

    @RabbitListener(queues = CONFIRM_QUEUE)
    public void receiveMsg(Message message){
        String msg = new String(message.getBody());
        log.info("消费者消费消息：{}",msg);
    }
}
