package com.sycn;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.DescriptorAccess;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * 异步消息发送生产者
 *
 * @author lzs
 * @version 1.0
 * @date 2022/1/8 15:13
 */
public class AsyncProducer {

    private static  final Logger logger = LoggerFactory.getLogger(AsyncProducer.class);

    public static void main(String[] args) {
        //创建消息生产者，设置生产者组名称pg
        DefaultMQProducer producer = new DefaultMQProducer("pg");
        //设置nameserver地址
        producer.setNamesrvAddr("192.168.0.113:9876");
        //设置消息发送失败的重试次数，默认2次
        producer.setRetryTimesWhenSendFailed(3);
        //设置topic的queue的数量，默认为4
        producer.setDefaultTopicQueueNums(2);
        //启动生产者
        try {
            producer.start();
            //创建消息
            for (int i = 0; i < 100; i++) {
                //创建消息体
                byte[] body = ("async message-" + i).getBytes(StandardCharsets.UTF_8);
                Message message = new Message("asyncMessageTopic", "asyncTag", body);
                //异步发送消息,指定回调
                try {
                    producer.send(message, new SendCallback() {
                        //消息发送成功回调方法
                        @Override
                        public void onSuccess(SendResult sendResult) {
                            System.out.println(sendResult);
                        }

                        //消息发送异常回调方法
                        @Override
                        public void onException(Throwable throwable) {
                            System.out.println(throwable);
                        }
                    });
                } catch (RemotingException e) {
                    logger.error("异步发送消息失败RemotingException"+e);
                } catch (InterruptedException e) {
                    logger.error("异步发送消息失败InterruptedException"+e);
                }
            }
            try {
                //采用异步发送消息不能立马关闭生产者
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (MQClientException e) {
            logger.error("启动生产者异常"+e);
        }finally {
            //关闭发送者
            producer.shutdown();
        }

    }
}
