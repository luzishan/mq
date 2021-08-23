package com.snym.priority;


import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.snym.utils.RabbitmqUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 队列优先级生产者
 *
 * @author lzs
 * @version 1.0
 * @date 2021/8/21 16:52
 */
public class PriorityProducer {

    public static final String QUEUE_PROIRITY= "queue_priority";

    public static void main(String[] args) {
        Channel channel = RabbitmqUtil.getChannel();
        //设置消息的优先级
        AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().priority(5).build();
        try {
            for (int i = 1; i < 11; i++) {
                String msg = "priority"+"-"+i;
                if(i == 5){
                    channel.basicPublish("",QUEUE_PROIRITY,properties,msg.getBytes(StandardCharsets.UTF_8));
                }else{
                    channel.basicPublish("",QUEUE_PROIRITY,null,msg.getBytes(StandardCharsets.UTF_8));
                }
                System.out.println("生产者发送消息："+msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
