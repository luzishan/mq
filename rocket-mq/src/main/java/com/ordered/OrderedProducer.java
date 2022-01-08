package com.ordered;

import com.sun.org.apache.xpath.internal.operations.Or;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 顺序消息生产者
 *
 * @author lzs
 * @version 1.0
 * @date 2022/1/8 15:55
 */
public class OrderedProducer {

    private static  final Logger logger = LoggerFactory.getLogger(OrderedProducer.class);

    public static void main(String[] args) {
        DefaultMQProducer producer = new DefaultMQProducer("pg");
        producer.setNamesrvAddr("192.168.0.113:9876");
        try {
            producer.start();
            for (int i = 0; i < 100; i++) {
                //模拟以订单id作为选择key
                int orderId = i;
                byte[] body = ("order message-" + i).getBytes(StandardCharsets.UTF_8);
                Message message = new Message("orderTopic", "orderTag", body);
                try {
                    SendResult send = producer.send(message, new MessageQueueSelector() {
                        //这里的arg就是传入的orderId
                        @Override
                        public MessageQueue select(List<MessageQueue> list, Message message, Object arg) {
                            Integer id = (Integer) arg;
                            //选择需要发送到的queue
                            int index = id % list.size();
                            return list.get(index);
                        }
                    }, orderId);
                    System.out.println(send);
                } catch (RemotingException e) {
                    logger.error("消息发送异常RemotingException"+e);
                } catch (MQBrokerException e) {
                    logger.error("消息发送异常MQBrokerException"+e);
                } catch (InterruptedException e) {
                    logger.error("消息发送异常InterruptedException"+e);
                }
            }
        } catch (MQClientException e) {
            logger.error("启动生产者异常"+e);
        }finally {
            producer.shutdown();
        }
    }
}
