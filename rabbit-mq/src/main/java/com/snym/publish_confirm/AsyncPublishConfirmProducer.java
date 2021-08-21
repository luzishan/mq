package com.snym.publish_confirm;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.snym.utils.RabbitmqUtil;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * 异步发布确认生产者:发布1000条异步确认消息，耗时73ms
 *
 * @author lzs
 * @version 1.0
 * @date 2021/8/18 21:59
 */
public class AsyncPublishConfirmProducer {

    private final static int MSG_COUNT = 1000;

    public static void main(String[] args) {
        Channel channel = RabbitmqUtil.getChannel();
        String queueName = UUID.randomUUID().toString();
        try {
            channel.queueDeclare(queueName, true, false, false, null);
            //开启发布确认
            channel.confirmSelect();
            /**
             * 线程安全有序的hash表，适用于高并发情况
             * 1.轻松的将序号和消息进行关联
             * 2.轻松的批量删除条目，只要给到序列号
             * 3.支持并发访问
             */
            ConcurrentSkipListMap<Long, String> confirms = new ConcurrentSkipListMap<>();
            /**
             * 确认收到消息的一个回调
             * 1.消息序列号
             * 2.true可以确认小于等于当前序号的消息
             *   false确认当前序号消息
             */
            ConfirmCallback ackConfirmCallback = (long deliveryTag, boolean multiple)->{
                if(multiple){
                    //返回的是小于或等于当前序号确认消息，是一个map
                    ConcurrentNavigableMap<Long, String> navigableMap = confirms.headMap(deliveryTag, true);
                    //清除该部分确认消息
                    navigableMap.clear();
                }else{
                    confirms.remove(deliveryTag);
                }
            };
            //未确认消息
            ConfirmCallback nackConfirmCallback = (long deliveryTag, boolean multiple)->{
                String msg = confirms.get(deliveryTag);
                System.out.println("发布的消息："+msg+"未被确认，序号："+deliveryTag);
            };
            //添加一个异步确认的监听器，确认收到消息的回调，未收到消息的回调
            channel.addConfirmListener(ackConfirmCallback,nackConfirmCallback);
            long beginTime = System.currentTimeMillis();
            for (int i = 0; i < MSG_COUNT; i++) {
                String message = "async publish confirm"+"-"+i;
                //记录所有发送的消息
                confirms.put(channel.getNextPublishSeqNo(),message);
                channel.basicPublish("",queueName,null,message.getBytes(StandardCharsets.UTF_8));
            }
            long endTime = System.currentTimeMillis();
            System.out.println("发布" + MSG_COUNT + "条异步确认消息，耗时" + (endTime - beginTime) + "ms");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
