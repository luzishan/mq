package com.work;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import com.utils.RabbitmqUtil;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 消费者01
 *
 * @author lzs
 * @version 1.0
 * @date 2021/8/18 20:41
 */
public class WorkConsumer01 {

    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException {
        Channel channel = RabbitmqUtil.getChannel();
        System.out.println("C1等待接收消息。。。。。。");
        DeliverCallback deliverCallback = (String consumerTag, Delivery message)->{
            String msg = new String(message.getBody());
            //System.out.println(msg);
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //deliveryTag:该消息的index；
            //multiple：是否批量true：将一次性ack所有小于deliveryTag的消息；确认收到消息
            System.out.println("C1消费消息："+msg);
            channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
        };
        CancelCallback cancelCallback = (String consumerTag)->{
            System.out.println("取消消息消费！");
        };
        Boolean autoAck = false;
        int prefetchCount = 3;
        channel.basicQos(prefetchCount);
        channel.basicConsume(QUEUE_NAME,autoAck, deliverCallback,cancelCallback);
    }
}
