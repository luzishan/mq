package com.snym.publish_confirm;

import com.rabbitmq.client.Channel;
import com.snym.utils.RabbitmqUtil;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * 单独发布确认生产者:发布1000条单独确认消息，耗时937ms
 *
 * @author lzs
 * @version 1.0
 * @date 2021/8/18 21:59
 */
public class SyncPublishConfirmProducer {

    private final static int MSG_COUNT = 1000;

    public static void main(String[] args) {
        Channel channel = RabbitmqUtil.getChannel();
        String queueName = UUID.randomUUID().toString();
        try {
            channel.queueDeclare(queueName, true, false, false, null);
            //开启发布确认
            channel.confirmSelect();
            long beginTime = System.currentTimeMillis();
            for (int i = 0; i < MSG_COUNT; i++) {
                String message = "publish confirm" + "-" + i;
                channel.basicPublish("", queueName, null, message.getBytes(StandardCharsets.UTF_8));
                //broker端返回false或者超时没有返回，生产者可以重发消息
                boolean flag = channel.waitForConfirms();
                if (flag) {
                    System.out.println("消息发送成功：" + message);
                }
            }
            long endTime = System.currentTimeMillis();
            System.out.println("发布" + MSG_COUNT + "条单独确认消息，耗时" + (endTime - beginTime) + "ms");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
