package com.sycn;

import org.apache.rocketmq.client.consumer.DefaultLitePullConsumer;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 消息消费者
 *
 * @author lzs
 * @version 1.0
 * @date 2022/1/8 15:36
 */
public class MessageConsumer {

    private static final Logger loger = LoggerFactory.getLogger(MessageConsumer.class);

    public static void main(String[] args) {
        //创建一个pull方式的消费者,并设置消费者组
        //DefaultLitePullConsumer pullConsumer = new DefaultLitePullConsumer("cg");
        //创建一个push方式的消费者，并设置消费者组
        DefaultMQPushConsumer pushConsumer = new DefaultMQPushConsumer("cg");
        //设置nameserver
        pushConsumer.setNamesrvAddr("192.168.0.113:9876");
        //设置消息的消费位置
        pushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        try {
            //设置消费的topic和tag
            pushConsumer.subscribe("someTopic","someTag");
            //设置消费模式，广播消费和集群消费
            pushConsumer.setMessageModel(MessageModel.CLUSTERING);
            //注册消息监听器
            pushConsumer.registerMessageListener(new MessageListenerConcurrently() {
                //一旦broker中有了其订阅的消息就会触发该方法执行消费消息，返回值是当前消费者的消费状态
                @Override
                public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                    //逐条消费消息
                    for (MessageExt messageExt : list) {
                        System.out.println(messageExt);
                    }
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
            });
            //开启消费者
            pushConsumer.start();
            System.out.println("消费者启动......");
        } catch (MQClientException e) {
            loger.error("消息消费异常MQClientException"+e);
        }

    }
}
