package com.snym.transaction;

import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.common.message.Message;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 事物消息生产者
 *
 * @author lzs
 * @version 1.0
 * @date 2021/8/16 22:08
 */
public class TransactionProducer {

    public static void main(String[] args) throws Exception {
        TransactionMQProducer producer = new TransactionMQProducer("tpg");
        producer.setNamesrvAddr("192.168.0.142:9876");
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2, 5, 100, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(2000), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("transaction-producer-thread");
                return thread;
            }
        });
        //为生产者指定线程池
        producer.setExecutorService(threadPoolExecutor);
        //为生产者添加事物监听器
        producer.setTransactionListener(new ICBCTransactionListener());
        //启动生产者
        producer.start();
        String[] tags = {"tagA","tagB","tagC"};
        for (int i = 0; i < 3; i++) {
            byte[] body = ("transaction" + "-" + i).getBytes(StandardCharsets.UTF_8);
            Message msg = new Message("transactionTopic", tags[i], body);
            TransactionSendResult sendResult = producer.sendMessageInTransaction(msg, null);
            System.out.println("发送结果为："+sendResult.getSendStatus());

        }
        //producer.shutdown();
    }
}
