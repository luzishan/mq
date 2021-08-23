package com.backup_exchange;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 备份消费者
 *
 * @author lzs
 * @version 1.0
 * @date 2021/8/21 16:23
 */
@Component
@Slf4j
public class WarningConsumer {

    public static final String WARNING_QUEUE = "warning.queue";

    @RabbitListener(queues = WARNING_QUEUE)
    public void receiveWarning(Message message){
        String msg = new String(message.getBody());
        log.error("备份不可路由的信息：{}",msg);
    }
}
