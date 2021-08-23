package com.snym.priority;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import com.snym.utils.RabbitmqUtil;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * 队列优先级消费者，注意生产要新发送消息到优先级队列，优先级队列才能对消息排序
 *
 * @author lzs
 * @version 1.0
 * @date 2021/8/21 17:01
 */
public class PriorityConsumer {

    public static final String QUEUE_PROIRITY= "queue_priority";

    public static void main(String[] args) {
        Channel channel = RabbitmqUtil.getChannel();
        //设置队列的优先级，最大可设置255，一般设置1-10，如果设置太高比较消耗内存和CPU
        Map<String,Object> params = new HashMap<>();
        params.put("x-max-priority",10);
        try {
            channel.queueDeclare(QUEUE_PROIRITY,true,false,false,params);
            DeliverCallback deliverCallBack = (String consumerTag, Delivery message)->{
                String msg = new String(message.getBody());
                System.out.println("接收到的消息："+msg);
            };
            CancelCallback cancelCallback = (String consumerTag)->{
                System.out.println("无法消费的消息："+consumerTag);
            };
            channel.basicConsume(QUEUE_PROIRITY,true,deliverCallBack,cancelCallback);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
