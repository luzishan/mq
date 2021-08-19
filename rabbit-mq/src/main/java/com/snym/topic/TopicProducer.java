package com.snym.topic;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.snym.utils.RabbitmqUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 主题交换机生产者
 *
 * @author lzs
 * @version 1.0
 * @date 2021/8/19 0:51
 */
public class TopicProducer {
    private static final String EXCHANGE_NAME = "T";

    public static void main(String[] args) {
        Channel channel = RabbitmqUtil.getChannel();
        try {
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
            Map<String, String> routingKeys = new HashMap<>();
            routingKeys.put("quick.orange.rabbit","被队列 Q1Q2 接收到");
            routingKeys.put("lazy.orange.elephant","被队列 Q1Q2 接收到");
            routingKeys.put("quick.orange.fox","被队列 Q1 接收到");
            routingKeys.put("lazy.brown.fox","被队列 Q2 接收到");
            routingKeys.put("lazy.pink.rabbit","虽然满足两个绑定但只被队列 Q2 接收一次");
            routingKeys.put("quick.brown.fox","不匹配任何绑定不会被任何队列接收到会被丢弃");
            routingKeys.put("quick.orange.male.rabbit","是四个单词不匹配任何绑定会被丢弃");
            routingKeys.put("lazy.orange.male.rabbit","是四个单词但匹配 Q2");
            routingKeys.forEach((k,v)->{
                String routingKey = k;
                String message = v;
                try {
                    channel.basicPublish(EXCHANGE_NAME,routingKey,null,message.getBytes(StandardCharsets.UTF_8));
                    System.out.println("生产者发送消息："+"路由key："+routingKey+"消息："+message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
