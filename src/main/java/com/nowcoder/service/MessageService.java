package com.nowcoder.service;

import com.nowcoder.domain.Message;

import java.util.List;

/**
 * Created by xuery on 2016/8/26.
 */
public interface MessageService {
    int addMessage(Message message);

    /**
     * 根据conversationId获取站内信
     * @param conversationId
     * @param offset
     * @param limit
     * @return
     */
    List<Message> getConversationDetail(String conversationId, int offset, int limit);


    /**
     * 查询跟userId相关的站内信(包括接收和发送)
     * 并取userId和同一个用户的最近一条站内通信消息（多条则取最新的一条，用到嵌套和group by）--但是好像并不能取到最新的一条
     * 因为userid可能在和多个用户通信
     * @param userId
     * @param offset
     * @param limit
     * @return
     */
    List<Message> getConversationList(int userId, int offset, int limit);

    /**
     * 发给这个用户的站内信未读，是toId
     * @param userId
     * @param conversationId
     * @return
     */
    int getConvesationUnreadCount(int userId, String conversationId);

    int getConvesationCount(String conversationId);

    int updateMessageToRead(Message message);
}
