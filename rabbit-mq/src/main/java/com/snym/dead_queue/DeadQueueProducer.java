package com.snym.dead_queue;

import com.rabbitmq.client.Channel;
import com.snym.utils.RabbitmqUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 生产者
 *
 * @author lzs
 * @version 1.0
 * @date 2021/8/19 23:39
 */
public class DeadQueueProducer {

    private static final String NORMAL_EXCHANGE="normal_exchange";

    public static void main(String[] args) {
        Channel channel = RabbitmqUtil.getChannel();
       /* Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String next = scanner.next();
            try {
                //设置ttl时间，10000ms
                //AMQP.BasicProperties build = new AMQP.BasicProperties().builder().expiration("10000").build();
                channel.basicPublish(NORMAL_EXCHANGE,"zhangsan",null,next.getBytes(StandardCharsets.UTF_8));
                System.out.println("生产者发送消息："+next);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
        for (int i = 0; i < 10; i++) {
            try {
                String msg = "x-max-length"+"-"+i;
                channel.basicPublish(NORMAL_EXCHANGE,"zhangsan",null,msg.getBytes(StandardCharsets.UTF_8));
                System.out.println("生产者发送消息："+msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
