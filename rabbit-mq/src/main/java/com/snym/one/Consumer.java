package com.snym.one;

import com.rabbitmq.client.*;

/**
 * 消费者
 *
 * @author lzs
 * @version 1.0
 * @date 2021/8/18 20:23
 */
public class Consumer {

    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args) {
        //创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //设置连接属性
        factory.setHost("192.168.0.142");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("123");
        try {
            //获取连接
            Connection connection = factory.newConnection();
            //创建连接通道
            Channel channel = connection.createChannel();
            System.out.println("等待接收消息。。。。。。");
            //推送的消息如何进行消费的回调接口
            DeliverCallback deliverCallback = (consumerTag, message) -> {
                String msg = new String(message.getBody());
                System.out.println(msg);
            };
            //取消消费的回调接口，例如消费时队列被删除掉
            CancelCallback cancelCallback = (String consumerTag) -> {
                System.out.println("消息消费被中断！");
            };
            channel.basicConsume(QUEUE_NAME,true, deliverCallback, cancelCallback);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }
}
