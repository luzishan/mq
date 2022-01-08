package com.delay;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 延时消息生产者
 *
 * @author lzs
 * @version 1.0
 * @date 2022/1/8 16:16
 */
public class DelayProducer {

    private static final Logger logger = LoggerFactory.getLogger(DelayProducer.class);

    public static void main(String[] args) {
        DefaultMQProducer producer = new DefaultMQProducer("pg");
        producer.setNamesrvAddr("192.168.0.113:9876");
        try {
            producer.start();
            for (int i = 0; i < 10; i++) {
                byte[] body = ("delay message-" + i).getBytes(StandardCharsets.UTF_8);
                Message message = new Message("delayTopic", "delayTag", body);
                //指定消息的延时等级,延时等级3，延时时间是10s
                message.setDelayTimeLevel(3);
                try {
                    SendResult send = producer.send(message);
                    System.out.println(new SimpleDateFormat("mm:ss").format(new Date()));
                    System.out.println(","+send);
                } catch (MQClientException mqClientException) {
                    logger.error("发送延时消息失败MQClientException"+mqClientException);
                } catch (RemotingException remotingException) {
                    logger.error("发送延时消息失败RemotingException"+remotingException);
                } catch (MQBrokerException mqBrokerException) {
                    logger.error("发送延时消息失败MQBrokerException"+mqBrokerException);
                } catch (InterruptedException interruptedException) {
                    logger.error("发送延时消息失败InterruptedException"+interruptedException);
                }
            }
        } catch (MQClientException e) {
            logger.error("启动消费者异常"+e);
        }finally {
            producer.shutdown();
        }
    }
}
