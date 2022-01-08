package com.transaction;

import io.netty.util.concurrent.DefaultThreadFactory;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.*;

/**
 * 事务消息生产者
 *
 * @author lzs
 * @version 1.0
 * @date 2022/1/8 17:15
 */
public class TransactionProducer {

    private static final Logger logger = LoggerFactory.getLogger(TransactionProducer.class);

    public static void main(String[] args) {
        TransactionMQProducer producer = new TransactionMQProducer("tpg");
        producer.setNamesrvAddr("192.168.0.113:9876");
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2,
                5,
                100,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(2000),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());
        //为生产者指定线程池
        producer.setExecutorService(threadPoolExecutor);
        //为生产者添加事务监听器
        producer.setTransactionListener(new ICBCTransactionListener());
        //启动生产者
        try {
            producer.start();
            String[] tags = {"tagA","tagB","tagC"};
            for (int i = 0; i < 3; i++) {
                byte[] body = ("transaction message-" + i).getBytes(StandardCharsets.UTF_8);
                Message message = new Message("transactionTopic", tags[i], body);
                TransactionSendResult transactionSendResult = producer.sendMessageInTransaction(message, null);
                System.out.println("消息发送结果："+transactionSendResult.getSendStatus());
            }
        } catch (MQClientException e) {
            logger.error("启动生产者异常"+e);
        } finally {
            //producer.shutdown();
        }
    }
}
