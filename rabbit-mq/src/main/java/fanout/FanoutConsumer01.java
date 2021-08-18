package fanout;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import utils.RabbitmqUtil;

import java.io.IOException;

/**
 * 扇形交换机消费者01
 *
 * @author lzs
 * @version 1.0
 * @date 2021/8/18 23:40
 */
public class FanoutConsumer01 {
    private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) {
        Channel channel = RabbitmqUtil.getChannel();
        //声明交换机
        try {
            channel.exchangeDeclare(EXCHANGE_NAME,"fanout");
            //生产临时队列
            String queue = channel.queueDeclare().getQueue();
            //绑定交换机和队列,扇形交换和队列的绑定路由键可以为""
            channel.queueBind(queue,EXCHANGE_NAME,"");
            System.out.println("FanoutConsumer01等待接收消息，把消息打印在控制台上......");
            DeliverCallback deliverCallback = (String consumerTag, Delivery message)->{
                String mesaage = new String(message.getBody());
                System.out.println("FanoutConsumer01控制台接收打印接收到的消息："+mesaage);
            };
            channel.basicConsume(queue,deliverCallback,(String consumerTag)->{});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
