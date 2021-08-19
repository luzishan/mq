package com.snym.direct;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.snym.utils.RabbitmqUtil;

import java.io.IOException;

/**
 * 直接交换机消费者01
 *
 * @author lzs
 * @version 1.0
 * @date 2021/8/18 23:57
 */
public class DirectConsumer01 {
    private static final String EXCHANGE_NAME = "X";
    private static final String QUEUE_NAME = "disk";

    public static void main(String[] args) {
        Channel channel = RabbitmqUtil.getChannel();
        try {
            //声明交换机
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
            //声明队列
            channel.queueDeclare(QUEUE_NAME,true,false,false,null);
            //绑定交换机和队列
            channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,"error");
            System.out.println("DirectConsumer01开始接收的消息......");
            DeliverCallback deliverCallback = (consumerTag,message)->{
                String msg = new String(message.getBody());
                System.out.println("DirectConsumer01消费的消息："+msg);
            };
            channel.basicConsume(QUEUE_NAME,deliverCallback,(String consumerTag)->{});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
