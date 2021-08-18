package publishconfirm;

import com.rabbitmq.client.Channel;
import utils.RabbitmqUtil;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * 批量发布确认生产者:发布1000条批量确认消息，耗时102ms
 *
 * @author lzs
 * @version 1.0
 * @date 2021/8/18 21:59
 */
public class BatchPublishConfirmProducer {

    private final static int MSG_COUNT = 1000;

    public static void main(String[] args) {
        Channel channel = RabbitmqUtil.getChannel();
        String queueName = UUID.randomUUID().toString();
        try {
            channel.queueDeclare(queueName, true, false, false, null);
            //开启发布确认
            channel.confirmSelect();
            //批量确认消息大小
            int batchCount = 100;
            //未被确认消息个数
            int outStandingMsgCount = 0;
            long beginTime = System.currentTimeMillis();
            for (int i = 0; i < MSG_COUNT; i++) {
                String message = "publish confirm" + "-" + i;
                channel.basicPublish("", queueName, null, message.getBytes(StandardCharsets.UTF_8));
                //broker端返回false或者超时没有返回，生产者可以重发消息
                outStandingMsgCount++;
                if(outStandingMsgCount == batchCount){
                     channel.waitForConfirms();
                    outStandingMsgCount = 0;
                }
            }
            if(outStandingMsgCount > 0){
                channel.waitForConfirms();
            }
            long endTime = System.currentTimeMillis();
            System.out.println("发布" + MSG_COUNT + "条批量确认消息，耗时" + (endTime - beginTime) + "ms");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
