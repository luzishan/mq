package com.sync;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * 同步消息发送，发一条确认一条
 *
 * @author lzs
 * @version 1.0
 * @date 2021/8/15 22:12
 */
public class SyncProducer {

    public static void main(String[] args) throws Exception {
        //创建一个producer，参数producer group名称
        DefaultMQProducer producer = new DefaultMQProducer("pg");
        //指定nameserver地址
        producer.setNamesrvAddr("192.168.0.142:9876");
        //设置当发送失败时重试的次数,默认是2次
        producer.setRetryTimesWhenSendFailed(3);
        //设置发送超时时间，默认是3s
        producer.setSendMsgTimeout(5000);
        //开启生产者
        producer.start();
        //生产并发送一百条消息
        for (int i = 0; i < 100; i++) {
            byte[] body = ("hello rocketmq" + i).getBytes(StandardCharsets.UTF_8);
            Message message = new Message("someTopic", "someTag", body);
            message.setKeys(UUID.randomUUID().toString()+i);
            //发送消息
            SendResult send = producer.send(message);
            System.out.println(send);
        }
        //关闭producer
        producer.shutdown();

    }
}
