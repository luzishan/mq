package com.snym.fanout;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.snym.utils.RabbitmqUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * 生产者
 *
 * @author lzs
 * @version 1.0
 * @date 2021/8/18 23:48
 */
public class FanoutProducer {
    private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) {
        Channel channel = RabbitmqUtil.getChannel();
        try {
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNext()){
                String msg = scanner.next();
                channel.basicPublish(EXCHANGE_NAME,"",null,msg.getBytes(StandardCharsets.UTF_8));
                System.out.println("生产者发送消息："+msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
