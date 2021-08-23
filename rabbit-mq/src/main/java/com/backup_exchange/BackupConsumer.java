package com.backup_exchange;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 报警消费者
 *
 * @author lzs
 * @version 1.0
 * @date 2021/8/21 16:23
 */
@Component
@Slf4j
public class BackupConsumer {

    public static final String BACKUP_QUEUE = "backup.queue";

    @RabbitListener(queues = BACKUP_QUEUE)
    public void receiveWarning(Message message){
        String msg = new String(message.getBody());
        log.error("报警发现不可路由的信息：{}",msg);
    }
}
