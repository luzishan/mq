package com.delay;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 延时消费者
 *
 * @author lzs
 * @version 1.0
 * @date 2021/8/16 19:39
 */
public class DelayConsumer {
    public static void main(String[] args) throws Exception{
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("cg");
        consumer.setNamesrvAddr("192.168.0.142:9876");
        //设置从哪消费
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        consumer.subscribe("delayTopic","delayTag");
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext context) {
                for (MessageExt messageExt : list) {
                    byte[] body = messageExt.getBody();
                    System.out.println(new String(body));
                    System.out.println(new SimpleDateFormat("hh:mm:ss").format(new Date()));
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                //return null;
            }
        });
        consumer.start();
    }
}
