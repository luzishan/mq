package direct;

import com.rabbitmq.client.Channel;
import utils.RabbitmqUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * 直接交换机生产者
 *
 * @author lzs
 * @version 1.0
 * @date 2021/8/19 0:07
 */
public class DirectProducer {
    private static final String EXCHANGE_NAME = "X";

    public static void main(String[] args) {
        Channel channel = RabbitmqUtil.getChannel();
        Scanner scanner = new Scanner(System.in);
        String msg = "";
        String routingKey = "";
        while (scanner.hasNext()) {
            String next = scanner.next();
            try {
                if ((Integer.valueOf(next) % 2) == 0) {
                    msg = "error" + "-" + next;
                    routingKey = "error";
                } else {
                    if ((Integer.valueOf(next) % 3) == 0) {
                        msg = "info" + "-" + next;
                        routingKey = "info";
                    } else {
                        msg = "warning" + "-" + next;
                        routingKey = "warning";
                    }

                }
                channel.basicPublish(EXCHANGE_NAME, routingKey, null, msg.getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("生产者发送消息：" + msg);
        }
    }
}
