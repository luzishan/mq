package com.filter;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

import java.nio.charset.StandardCharsets;

/**
 * 过滤消息生产者
 *
 * @author lzs
 * @version 1.0
 * @date 2022/1/8 19:03
 */
public class FilterBySqlProducer {

    public static void main(String[] args) throws Exception{
        DefaultMQProducer producer = new DefaultMQProducer("pg");
        producer.setNamesrvAddr("192.168.0.113:9876");
        producer.start();
        String[] tags = {"tagA","tagB","tagC"};
        for (int i = 0; i < 3; i++) {
            byte[] body = ("filter message - " + i).getBytes(StandardCharsets.UTF_8);
            Message message = new Message("filterTopic", tags[i], body);
            message.putUserProperty("age",i+"");
            SendResult send = producer.send(message);
            System.out.println(send);
        }
        producer.shutdown();
    }
}
