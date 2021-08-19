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
 * @date 2021/8/16 21:55
 */
public class ICBCTransactionListener implements TransactionListener {
    /**
     * @description: 回调操作方法
     *               消息预提交成功后就会触发该方法的执行，用于完成本地事物
     * @param: message
     * @param: o
     * @return: org.apache.rocketmq.client.producer.LocalTransactionState
     * @author: lzs
     * @date: 2021/8/16 21:56
     */
    @Override
    public LocalTransactionState executeLocalTransaction(Message message, Object o) {
        System.out.println("预提交成功："+message);
        /**
         * 1.假设接收到tagA的消息就表示扣款成功操作
         * 2.tagB的消息表示扣款失败操作
         * 3.tagC表示扣款结果不清楚操作，需要执行消息的回查
         */
        if(StringUtils.equals("tagA",message.getTags())){
            return LocalTransactionState.COMMIT_MESSAGE;
        }else if(StringUtils.equals("tagB",message.getTags())){
            return LocalTransactionState.ROLLBACK_MESSAGE;
        }else if(StringUtils.equals("tagC",message.getTags())){
            return LocalTransactionState.UNKNOW;
        }
        return LocalTransactionState.UNKNOW;
    }

    /**
     * 回查方法，产生回查的原因有二
     * 1.回调操作返回unknown
     * 2.tc没有接收到tm的最终全局事物指令
     *
     */
    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
        System.out.println("执行消息回查："+messageExt.getTags());
        return LocalTransactionState.COMMIT_MESSAGE;
    }
}
