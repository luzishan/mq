package com.snym.async;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 异步消息发送，异步确认
 *
 * @author lzs
 * @version 1.0
 * @date 2021/8/15 23:16
 */
public class AsyncProducer {
    public static void main(String[] args) throws Exception{
        //创建一个生产者
        DefaultMQProducer producer = new DefaultMQProducer("pg");
        //设置nameserver地址和端口
        producer.setNamesrvAddr("192.168.0.142:9876");
        //指定异步发送失败后不进行重试发送
        producer.setRetryTimesWhenSendFailed(0);
        //指定创建Topic中队列的数量，默认为4
        producer.setDefaultTopicQueueNums(2);
        //启动生产者
        producer.start();
        for (int i = 0; i < 100; i++) {
            byte[] body = ("hello rocketmq" + i).getBytes(StandardCharsets.UTF_8);
            Message message = new Message("myTopic", "myTag", body);
            //设置message的key
            message.setKeys(UUID.randomUUID().toString()+"-"+i);
            producer.send(message, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    System.out.println(sendResult);
                    System.out.println(sendResult.getSendStatus());
                }

                @Override
                public void onException(Throwable e) {
                    e.printStackTrace();
                }
            });

        }
        TimeUnit.SECONDS.sleep(3);
        producer.shutdown();
    }
}
