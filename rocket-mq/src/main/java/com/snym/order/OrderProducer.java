package com.snym.order;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 顺序消息的分区有序
 *
 * @author lzs
 * @version 1.0
 * @date 2021/8/16 0:29
 */
public class OrderProducer {

    public static void main(String[] args) throws Exception{
        //创建生产者
        DefaultMQProducer producer = new DefaultMQProducer("pg");
        //设置nameserver
        producer.setNamesrvAddr("192.168.0.142:9876");
        //启动生产者
        producer.start();
        for (int i = 0; i < 100; i++) {
            //订单id
           Integer orderId = i;
            byte[] body = ("order-" + i).getBytes(StandardCharsets.UTF_8);
            Message message = new Message("topicA", "tagA", body);
            message.setKeys(orderId.toString());
            //agr就是orderId
            SendResult result = producer.send(message, new MessageQueueSelector() {
                @Override
                public MessageQueue select(List<MessageQueue> list, Message message, Object arg) {
                    //String keys = message.getKeys();
                    //Integer key = Integer.valueOf(keys);
                    //int index key % list.size();
                    Integer id = (Integer) arg;
                    int index = id % list.size();
                    return list.get(index);
                }
            }, orderId);
            System.out.println(result);
        }
        producer.shutdown();
    }
}
