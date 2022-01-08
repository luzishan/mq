package com.sycn;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

/**
 * 同步消息发送者
 *
 * @author lzs
 * @version 1.0
 * @date 2022/1/8 14:52
 *
 *   SEND_OK:消息发送成功
 *   FLUSH_DISK_TIMEOUT:消息刷盘超时，当broker设置的刷盘策略为同步刷盘时才会出现，异步刷盘不会出现
 *   FLUSH_SLAVE_TIMEOUT:slave同步超时，当broker集群设置的master-slave的复制方式是同步复制才可能出现的异常状态，异步复制不会出现
 *   SLAVE_NOT_AVAILABLE:没有可用的slave，当broker集群设置为master-slave的复制方式为同步复制时才可能出现的异常状态，异步复制不会出现
 */
public class SyncProducer {

    private static final Logger logger = LoggerFactory.getLogger(SyncProducer.class);

    public static void main(String[] args) {
        //创建一个producer，参数为producer group名称
        DefaultMQProducer producer = new DefaultMQProducer("pg");
        //指定nameserver地址
        producer.setNamesrvAddr("192.168.0.113:9876");
        //设置当发送失败的重试次数，默认为2次
        producer.setRetryTimesWhenSendFailed(3);
        //设置发送超时时间5s,默认为3s
        producer.setSendMsgTimeout(5000);
        //开启生产者
        try {
            producer.start();
            //生产并发送100条消息
            for (int i = 0; i < 100; i++) {
                byte[] body = ("hello rocketmq" + "-" + i).getBytes(StandardCharsets.UTF_8);
                //设置topic和tag
                Message message = new Message("someTopic", "someTag", body);
                //指定消息的key
                message.setKeys("key-"+i);
                try {
                    SendResult send = producer.send(message);
                    System.out.println(send);
                } catch (RemotingException e) {
                    logger.error("发送消息失败RemotingException"+e);
                } catch (MQBrokerException e) {
                    logger.error("发送消息失败MQBrokerException"+e);
                } catch (InterruptedException e) {
                    logger.error("发送消息失败InterruptedException"+e);
                }
            }
        } catch (MQClientException e) {
            logger.error("开启生产者失败"+e);
            e.printStackTrace();
        }finally {
            //关闭生产者
            producer.shutdown();
        }

    }
}
