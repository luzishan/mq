package com.callback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 消息发送交换机接收到回调和路由不匹配返回给生产者回调
 *
 * @author lzs
 * @version 1.0
 * @date 2021/8/21 1:05
 */
@Component
@Slf4j
public class ConfirmAndReturnCallBack implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    //依赖注入rabbitTemplate后设置回调对象
    @PostConstruct
    public void init(){
        //设置回调
        rabbitTemplate.setConfirmCallback(this);
        //true：交换机无法将消息路由时，将消息返回给生产者
        //false：交换机无法将消息路由时，将消息丢弃
        //rabbitTemplate.setMandatory(true);
        //设置回退消息处理
        rabbitTemplate.setReturnCallback(this);
    }

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


    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        log.info("消息：{}，被交换机：{}退回，退回原因：{}，路由key:{}",new String(message.getBody()),exchange,replyText,routingKey);
    }



}
