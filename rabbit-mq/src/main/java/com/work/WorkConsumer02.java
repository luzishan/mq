package com.work;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import com.utils.RabbitmqUtil;

import java.util.concurrent.TimeUnit;

/**
 * 消费者01
 *
 * @author lzs
 * @version 1.0
 * @date 2021/8/18 20:41
 */
public class WorkConsumer02 {

    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitmqUtil.getChannel();
        System.out.println("C2等待接收消息。。。。。。");
        DeliverCallback deliverCallback = (String consumerTag, Delivery message)->{
            String msg = new String(message.getBody());
            //System.out.println(msg);
            try {
                TimeUnit.SECONDS.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("C2消费消息："+msg);
            //deliveryTag:该消息的index；
            //multiple：是否批量true：将一次性ack所有小于deliveryTag的消息；确认收到消息
            channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
        };
        CancelCallback cancelCallback = (String consumerTag)->{
            System.out.println("取消消息消费！");
        };
        //手动应答
        Boolean autoAck = false;
        //预取值
        int prefetchCount = 1;
        channel.basicQos(prefetchCount);
        channel.basicConsume(QUEUE_NAME,autoAck, deliverCallback,cancelCallback);
    }
}
