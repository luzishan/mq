package com.batch;

import org.apache.rocketmq.common.message.Message;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 消息分割器
 *
 * @author lzs
 * @version 1.0
 * @date 2022/1/8 18:28
 */
public class MessageListSpliter implements Iterator<List<Message>> {

    //指定消息大小为4M
    private final int SIZE_LIMIT = 4 * 1024 * 1024;
    //存放需要发送的消息
    private final List<Message> messageList;
    //要批量发送消息的小集合的起始索引
    private int currIndex;

    public MessageListSpliter(List<Message> messageList) {
        this.messageList = messageList;
    }


    @Override
    public boolean hasNext() {
        //判断当前开始遍历的消息索引要小于消息总数
        return currIndex < messageList.size();
    }

    @Override
    public List<Message> next() {
        int nextIndex = currIndex;
        //记录每一批次发送消息列表的大小
        int totalSize = 0;
        for (; nextIndex < messageList.size(); nextIndex++) {
            //获取当前遍历的消息
            Message message = messageList.get(nextIndex);
            //统计每条消息的大小
            AtomicInteger tempSize = new AtomicInteger(message.getBody().length + message.getTopic().length());
            Map<String, String> properties = message.getProperties();
            properties.forEach((k, v) -> {
                tempSize.addAndGet(k.length() + v.length());
            });
            tempSize.addAndGet(20);
            //判断当前消息本身是否大于4M
            if (tempSize.get() > SIZE_LIMIT) {
                if (nextIndex - currIndex == 0) {
                    nextIndex++;
                }
                break;
            }
            if (tempSize.get() + totalSize > SIZE_LIMIT) {
                break;
            } else {
                totalSize += tempSize.get();
            }
        }
        //获取当前消息集合的子集合[currIndex,nextIndex)
        List<Message> subList = messageList.subList(currIndex, nextIndex);
        //下次遍历开始索引
        currIndex = nextIndex;
        return subList;
    }
}
