package com.oneway;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * 单向发送，发送成功与否没有回复
 *
 * @author lzs
 * @version 1.0
 * @date 2021/8/15 23:29
 */
public class OnewayProducer {

    public static void main(String[] args) throws Exception {
        //创建生产者
        DefaultMQProducer producer = new DefaultMQProducer("pg");
        //设置nameserver的地址和端口
        producer.setNamesrvAddr("192.168.0.142:9876");
        //启动生产者
        producer.start();
        for (int i = 0; i < 100; i++) {
            byte[] body = ("hello rocketmq" + i).getBytes(StandardCharsets.UTF_8);
            Message message = new Message("singleTopic", "singleTag", body);
            message.setKeys(UUID.randomUUID().toString()+"-"+i);
            producer.sendOneway(message);
        }
        producer.shutdown();
    }
}
