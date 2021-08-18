package utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * rabbitm工具类
 *
 * @author lzs
 * @version 1.0
 * @date 2021/8/18 20:37
 */
public class RabbitmqUtil {

    public static Channel getChannel() {
        Channel channel = null;
        //创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //设置连接属性
        factory.setHost("192.168.0.142");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("123");
        try {
            //获取连接
            Connection  connection = factory.newConnection();
            //创建连接通道
            channel = connection.createChannel();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return channel;
    }
}
