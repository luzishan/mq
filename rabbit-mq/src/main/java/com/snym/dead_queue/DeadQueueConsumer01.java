package com.snym.dead_queue;


import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import com.snym.utils.RabbitmqUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 死信队列正常消费者
 *
 * @author lzs
 * @version 1.0
 * @date 2021/8/19 23:18
 */
public class DeadQueueConsumer01 {

    private static final String NORMAL_EXCHANGE = "normal_exchange";

    private static final String DEAD_EXCHANGE = "dead_exchange";

    private static final String NORMAL_QUEUE = "normal_queue";

    private static final String DEAD_QUEUE = "com/snym/dead_queue";

    public static void main(String[] args) {
        Channel channel = RabbitmqUtil.getChannel();
        try {
            //正常交换机
            channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
            //死信交换机
            channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);
            //死信队列
            channel.queueDeclare(DEAD_QUEUE, false, false, false, null);
            Map<String, Object> param = new HashMap<>();
            param.put("x-dead-letter-exchange", DEAD_EXCHANGE);
            param.put("x-dead-letter-routing-key", "lisi");
            //设置队列最大长度
            //param.put("x-max-length", 6);
            //正常队列
            channel.queueDeclare(NORMAL_QUEUE, false, false, false, param);

            //死信交换机和死信队列的绑定
            channel.queueBind(DEAD_QUEUE, DEAD_EXCHANGE, "lisi");
            channel.queueBind(NORMAL_QUEUE, NORMAL_EXCHANGE, "zhangsan");
            DeliverCallback deliverCallback = (String consumerTag, Delivery message) -> {
                String msg = new String(message.getBody());
                if ("x-max-length-5".equals(msg)) {
                    System.out.println("正常消费者接收到的消息，但是拒绝"+msg);
                    channel.basicReject(message.getEnvelope().getDeliveryTag(), false);
                }else{
                    channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
                    System.out.println("正常消费者消费的消息：" + msg);
                }
            };
            boolean autoAck = false;
            channel.basicConsume(NORMAL_QUEUE, autoAck, deliverCallback, (String consumerTag) -> {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
