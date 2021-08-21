package com.snym.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 备份交换机：交换机和队列的配置
 *
 * @author lzs
 * @version 1.0
 * @date 2021/8/21 15:55
 */
@Configuration
@Slf4j
public class BackupExchangeConfig {

    public static final String B_CONFIRM_EXCHANGE = "b.confirm.exchange";

    public static final String BACKUP_EXCHANGE = "backup.exchange";

    public static final String B_CONFIRM_QUEUE = "b.confirm_queue";

    public static final String BACKUP_QUEUE = "backup.queue";

    public static final String WARNING_QUEUE = "warning.queue";

    //声明确认交换机
    @Bean("bConfirmExchange")
    public DirectExchange bConfirmExchange() {
        ExchangeBuilder exchangeBuilder = ExchangeBuilder.directExchange(B_CONFIRM_EXCHANGE).durable(true)
                .withArgument("alternate-exchange", BACKUP_EXCHANGE);
        return exchangeBuilder.build();
    }

    //声明确认队列
    @Bean("bConfirQueue")
    public Queue bConfirQueue() {
        return QueueBuilder.durable(B_CONFIRM_QUEUE).build();
    }

    //绑定确认交换机和确认队列
    @Bean
    public Binding bConfirmQueueBindingBConfirmExchange(@Qualifier("bConfirQueue") Queue bConfirQueue,
                                                      @Qualifier("bConfirmExchange") DirectExchange bConfirmExchange) {
        return BindingBuilder.bind(bConfirQueue).to(bConfirmExchange).with("key1");
    }

    //声明备份交换机
    @Bean("backupExchange")
    public FanoutExchange backupExchange() {
        return new FanoutExchange(BACKUP_EXCHANGE);
    }

    //声明备份队列
    @Bean("backupQueue")
    public Queue backupQueue(){
        return QueueBuilder.durable(BACKUP_QUEUE).build();
    }

    //绑定备份交换机和备份队列
    @Bean
    public Binding backupQueueBindingBackupExchange(@Qualifier("backupQueue") Queue backupQueue,
                                                    @Qualifier("backupExchange") FanoutExchange backupExchange){
        return BindingBuilder.bind(backupQueue).to(backupExchange);

    }

    //声明警告队列
    @Bean("warningQueue")
    public Queue warningQueue(){
        return QueueBuilder.durable(WARNING_QUEUE).build();
    }

    //绑定备份交换机和警告队列
    @Bean
    public Binding warningQueueBindingBackupExchange(@Qualifier("warningQueue") Queue warningQueue,
                                                    @Qualifier("backupExchange") FanoutExchange backupExchange){
        return BindingBuilder.bind(warningQueue).to(backupExchange);

    }

}
