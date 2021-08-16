package com.snym.sync;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

import java.util.List;

/**
 * 消费者
 *
 * @author lzs
 * @version 1.0
 * @date 2021/8/15 23:37
 */
public class SyncConsumer {

    public static void main(String[] args) throws Exception{
        //定义一个pull消费者
        //DefaultLitePullConsumer consumer = new DefaultLitePullConsumer("cg");
        //定义一个push消费者
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("cg");
        //设置nameserver
        consumer.setNamesrvAddr("192.168.0.142:9876");
        //指定从第一条消息开始消费
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        //指定消费的topic和tag
        consumer.subscribe("someTopic","*");
        //指定消费模式，广播模式或集群模式，默认是集群模式
        //consumer.setMessageModel(MessageModel.BROADCASTING);
        consumer.setMessageModel(MessageModel.CLUSTERING);
        //注册消息监听器
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            //一旦broker中有了其订阅的消息就会触发该方法的执行，返回值是当前consumer消费的状态
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext context) {
                //逐条消费
                for (MessageExt msg : list) {
                    System.out.println(msg);
                }
                //返回消费状态
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        //开启消费者
        consumer.start();
        System.out.println("consumer started");
    }
}
