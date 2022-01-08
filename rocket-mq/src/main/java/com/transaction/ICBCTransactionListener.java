package com.transaction;

import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

/**
 * 事物监听器
 *
 * @author lzs
 * @version 1.0
 * @date 2022/1/8 17:05
 */
public class ICBCTransactionListener implements TransactionListener {

    //回调方法，消息预提交成功后悔触发该方法的执行，用于完成本地事物
    @Override
    public LocalTransactionState executeLocalTransaction(Message message, Object o) {
        System.out.println("预提交消息成功：" + message);
        //假设接收到tagA消息表示扣款成功，tagB表示扣款失败，tagC表示unknown
        if (StringUtils.equals("tagA", message.getTags())) {
            return LocalTransactionState.COMMIT_MESSAGE;
        } else if (StringUtils.equals("tagB", message.getTags())) {
            return LocalTransactionState.ROLLBACK_MESSAGE;
        } else if (StringUtils.equals("tagC", message.getTags())) {
            return LocalTransactionState.UNKNOW;
        }
        return LocalTransactionState.UNKNOW;
    }

    /**
     * 消息回查方法，回查的原因：1.回调操作返回unknown，2.tc没有接收到tm的最终全局事物确认指令
     */
    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
        System.out.println("执行消息回查：" + messageExt.getTags());
        return LocalTransactionState.COMMIT_MESSAGE;
    }
}
