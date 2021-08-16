package com.snym.delay;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.SimpleFormatter;

/**
 * 延时消息
 *
 * @author lzs
 * @version 1.0
 * @date 2021/8/16 19:32
 */
public class DelayProducer {
    public static void main(String[] args) throws Exception{
        DefaultMQProducer producer = new DefaultMQProducer("pg");
        producer.setNamesrvAddr("192.168.0.142:9876");
        producer.start();
        for (int i = 0; i < 1; i++) {
            byte[] body = ("delay" + i).getBytes(StandardCharsets.UTF_8);
            Message message = new Message("delayTopic", "delayTag", body);
            //设置消息的延时等级3,延时10s
            message.setDelayTimeLevel(2);
            SendResult send = producer.send(message);
            System.out.println(send);
            System.out.println(new SimpleDateFormat("hh:mm:ss").format(new Date()));
        }
        producer.shutdown();
    }
}
