package com.sycn;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

/**
 * 单向发送消息生产者
 *
 * @author lzs
 * @version 1.0
 * @date 2022/1/8 15:28
 */
public class OneWayProducer {

    private static final Logger logger = LoggerFactory.getLogger(OneWayProducer.class);

    public static void main(String[] args) {
        //创建消息发送生产者,设置生产者组pg
        DefaultMQProducer producer = new DefaultMQProducer("pg");
        //设置nameserver
        producer.setNamesrvAddr("192.168.0.113:9876");
        try {
            producer.start();
            //创建消息
            for (int i = 0; i < 100; i++) {
                //创建消息体
                byte[] body = ("oneway message-" + i).getBytes(StandardCharsets.UTF_8);
                Message message = new Message("oneWayTopic", "oneWayTag", body);
                message.setKeys("key-"+i);
                try {
                    producer.sendOneway(message);
                } catch (RemotingException e) {
                    logger.error("发送消息失败RemotingException"+e);
                } catch (InterruptedException e) {
                    logger.error("发送消息失败InterruptedException"+e);
                }
            }
        } catch (MQClientException e) {
            logger.error("启动生产者失败"+e);
        }finally {
            producer.shutdown();
        }

    }
}
