package com.backup_exchange;

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
public class BackupExchangeConsumer {

    public static final String B_CONFIRM_QUEUE = "b.confirm_queue";

    @RabbitListener(queues = B_CONFIRM_QUEUE)
    public void receiveMsg(Message message){
        String msg = new String(message.getBody());
        log.info("消费者消费消息：{}",msg);
    }
}
