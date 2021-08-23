package com.dead_queue;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import com.utils.RabbitmqUtil;

import java.io.IOException;

/**
 * 死信队列死信消费者
 *
 * @author lzs
 * @version 1.0
 * @date 2021/8/19 23:18
 */
public class DeadQueueConsumer02 {

    private static final String DEAD_QUEUE = "com/dead_queue";

    public static void main(String[] args) {
        Channel channel = RabbitmqUtil.getChannel();
        try {
            DeliverCallback deliverCallback = (String consumerTag, Delivery message)->{
                String msg = new String(message.getBody());
                System.out.println("死信消费者消费的消息："+msg);
            };
            channel.basicConsume(DEAD_QUEUE,true,deliverCallback,(String consumerTag)->{});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
