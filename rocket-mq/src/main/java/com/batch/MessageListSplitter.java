package com.batch;

import org.apache.rocketmq.common.message.Message;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 定义消息列表分割器：其只会处理每条消息大小不超过4m的情况
 * 如果存在某条消息，其本身大小大于4m，则分割器无法处理
 * 直接将这条消息构成一个子列表返回，并没有再处理
 *
 * @author lzs
 * @version 1.0
 * @date 2021/8/16 23:00
 */
public class MessageListSplitter implements Iterator<List<Message>> {

    //指定极限值为4m
    private final int SIZE_LIMIT = 4 * 1024 * 1024;
    //存放所有要发送的消息
    private final List<Message> messages;
    //要进行批量发送消息的小集合索引
    private int currIndex;

    public MessageListSplitter(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public boolean hasNext() {
        //判断当前开始遍历的消息索引要小于消息总和
        return currIndex < messages.size();
    }

    @Override
    public List<Message> next() {
        int nextIndex = currIndex;
        //记录当前要发送的这一批次消息列表的大小
        int totalSize = 0;
        for (; nextIndex < messages.size(); nextIndex++) {
            //获取当前遍历的消息
            Message message = messages.get(nextIndex);
            //统计当前消息的大小
            int tempSize = message.getTags().length() + message.getBody().length;
            Map<String, String> properties = message.getProperties();
            for (Map.Entry<String, String> entry : properties.entrySet()) {
                tempSize += entry.getKey().length() + entry.getValue().length();
            }
            tempSize = totalSize + 20;
            //判断当前消息本身是否大于4m
            if (tempSize > SIZE_LIMIT) {
                if (nextIndex - currIndex == 0) {
                    nextIndex++;
                }
                break;
            }
            if (tempSize + totalSize > SIZE_LIMIT) {
                break;
            } else {
                totalSize += tempSize;
            }
        }
        //获取当前messages列表的子集合[currIndex,nextIndex)
        List<Message> messages = this.messages.subList(currIndex, nextIndex);
        //下次遍历开始的索引
        currIndex = nextIndex;
        return messages;
    }
}
