package com.batch;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 批量消息生产者
 *
 * @author lzs
 * @version 1.0
 * @date 2022/1/8 18:47
 */
public class BatchProducer {

    public static void main(String[] args) throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("pg");
        producer.setNamesrvAddr("192.168.0.113:9876");
        producer.start();
        List<Message> messageList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            byte[] body = ("batch message-" + i).getBytes(StandardCharsets.UTF_8);
            Message message = new Message("batchTopic", "batchTag", body);
            messageList.add(message);
        }
        //定义消息列表分割器，将消息列表分割成多个大小不超过4M的小列表
        MessageListSpliter spliter = new MessageListSpliter(messageList);
        while (spliter.hasNext()){
            List<Message> next = spliter.next();
            SendResult send = producer.send(next);
            System.out.println(send);
        }
        producer.shutdown();
    }
}
