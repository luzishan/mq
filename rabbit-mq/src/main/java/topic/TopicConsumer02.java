package topic;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import utils.RabbitmqUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * 主题交换机02
 *
 * @author lzs
 * @version 1.0
 * @date 2021/8/19 0:35
 */
public class TopicConsumer02 {
    private static final String EXCHANGE_NAME = "T";
    private static final String QUEUE_NAME = "Q2";

    public static void main(String[] args) {
        Channel channel = RabbitmqUtil.getChannel();
        try {
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
            channel.queueDeclare(QUEUE_NAME,true,false,false,null);
            List<String> routingKeys = Arrays.asList("*.*.rabbit","lazy.#");
            routingKeys.stream().forEach(routingKey->{
                //绑定交换机和队列
                try {
                    channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,routingKey);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            System.out.println("TopicConsumer02等待接收消息......");
            DeliverCallback deliverCallback = (String consumerTag, Delivery message)->{
                String msg = new String(message.getBody());
                System.out.println("TopicConsumer02消费的消息："+msg);
            };
            channel.basicConsume(QUEUE_NAME,deliverCallback,(String consumerTag)->{});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
