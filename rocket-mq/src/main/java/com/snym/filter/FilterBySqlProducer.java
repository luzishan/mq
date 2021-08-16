package com.snym.filter;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

import java.nio.charset.StandardCharsets;

/**
 * 消息过滤生产者
 *
 * @author lzs
 * @version 1.0
 * @date 2021/8/17 0:02
 */
public class FilterBySqlProducer {
    public static void main(String[] args) throws Exception{
        DefaultMQProducer producer = new DefaultMQProducer("pg");
        producer.setNamesrvAddr("192.168.0.142:9876");
        producer.start();
        for (int i = 0; i < 10; i++) {
            byte[] body = ("sql" + "-" + i).getBytes(StandardCharsets.UTF_8);
            Message message = new Message("sqlTopic", "sqlTag", body);
            message.putUserProperty("age",i+"");
            SendResult sendResult = producer.send(message);
            System.out.println(sendResult);
        }
        producer.shutdown();
    }
}
