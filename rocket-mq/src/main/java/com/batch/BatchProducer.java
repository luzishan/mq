package com.batch;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 批量生产者
 *
 * @author lzs
 * @version 1.0
 * @date 2021/8/16 23:24
 */
public class BatchProducer {
    public static void main(String[] args) throws Exception{
        DefaultMQProducer producer = new DefaultMQProducer("bpg");
        producer.setNamesrvAddr("192.168.0.142:9876");
        producer.start();
        //定义要发送的消息集合
        List<Message> messageList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            byte[] body = ("batch" + "-" + i).getBytes(StandardCharsets.UTF_8);
            Message message = new Message("batchTopic", "batchTag", body);
            messageList.add(message);
        }
        //定义消息列表分割器，将消息列表分割为多个不超过4m大小的小列表
        MessageListSplitter messageListSplitter = new MessageListSplitter(messageList);
        while (messageListSplitter.hasNext()){
            List<Message> next = messageListSplitter.next();
            SendResult send = producer.send(next);
            System.out.println(send);
        }
        producer.shutdown();

    }
}
