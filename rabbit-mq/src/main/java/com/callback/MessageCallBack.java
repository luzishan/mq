package com.callback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * 消息发送回调接口
 *
 * @author lzs
 * @version 1.0
 * @date 2021/8/21 1:05
 */
@Component
@Slf4j
public class MessageCallBack implements RabbitTemplate.ConfirmCallback {

    /**
     * 交换机不管是否收到消息的回调方法
     * correlationData：消息的相关参数
     * ack：交换机是否收到消息
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = correlationData != null?correlationData.getId():"";
        if(ack){
            log.info("交换机收到消息的id为：{}的消息",id);
        }else {
            log.info("交换机未收到消息的id为：{}的消息，原因：{}",id,cause);
        }
    }
}
