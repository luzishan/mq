package com.snym.one;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;

/**
 * 生产者
 *
 * @author lzs
 * @version 1.0
 * @date 2021/8/18 20:10
 */
public class Producer {

    //定义队列名字
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) {
        //创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //设置连接属性
        factory.setHost("192.168.0.142");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("123");
        try{
            //获取连接connection
            Connection connection = factory.newConnection();
            //获取连接通道channel
            Channel channel = connection.createChannel();
            /**生成一个队列
             * 1.队列名称
             * 2.队里里面的消息是否持久化，默认不持久化，存在内存当中
             * 3.该队列是否只供一个消费者消费，即是否共享，true表示可以多个消费者消费
             * 4.是否自动删除，即最后一个消费者断开连接后，该队列是否自动删除，true表示自动删除
             * 5.其他参数
             */
            channel.queueDeclare(QUEUE_NAME,false,false,false,null);
            String message = "hello rabbitmq";
            /**
             * 发送消息
             * 1.发送到哪个交换机
             * 2.交换机和队列之间的路由key,即routingKey
             * 3.其他参数
             * 4.发送的消息体
             */
            channel.basicPublish("",QUEUE_NAME,null,message.getBytes(StandardCharsets.UTF_8));
            System.out.println("消息发送完毕！");
        }catch (Exception e){

        }

    }
}
