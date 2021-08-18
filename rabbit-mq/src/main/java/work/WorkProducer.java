package work;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import utils.RabbitmqUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * 生产者
 *
 * @author lzs
 * @version 1.0
 * @date 2021/8/18 20:47
 */
public class WorkProducer {

    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args) {
        Channel channel = RabbitmqUtil.getChannel();
        //生产一个队列
       /* try {
            channel.queueDeclare(QUEUE_NAME,false,false, false,null);
            String message = "hello work rabbit";
            channel.basicPublish("", QUEUE_NAME,null,message.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        try {
            //队列持久化
            Boolean durable = true;
            channel.queueDeclare(QUEUE_NAME,durable,false, false,null);
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNext()){
                String next = scanner.next();
                String message = next;
                // MessageProperties.PERSISTENT_TEXT_PLAIN消息持久化
                channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes(StandardCharsets.UTF_8));
                System.out.println("消息发送完毕："+next);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
