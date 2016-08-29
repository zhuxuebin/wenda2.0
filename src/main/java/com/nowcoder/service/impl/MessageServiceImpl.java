package com.nowcoder.service.impl;

import com.nowcoder.dao.MessageMapper;
import com.nowcoder.domain.Message;
import com.nowcoder.service.MessageService;
import com.nowcoder.service.SensitiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by xuery on 2016/8/26.
 */
@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    MessageMapper messageMapper;

    @Autowired
    SensitiveService sensitiveService;

    @Override
    public int addMessage(Message message) {
        message.setContent(sensitiveService.filter(message.getContent()));
        return messageMapper.insert(message);
    }

    @Override
    public List<Message> getConversationDetail(String conversationId, int offset, int limit) {
        return messageMapper.getConversationDetail(conversationId,offset,limit);
    }

    @Override
    public List<Message> getConversationList(int userId, int offset, int limit) {
        return messageMapper.getConversationList(userId,offset,limit);
    }

    @Override
    public int getConvesationUnreadCount(int userId, String conversationId) {
        return messageMapper.getConvesationUnreadCount(userId,conversationId);
    }

    @Override
    public int updateMessageToRead(Message message){
        message.setHasRead(1);
       return  messageMapper.updateByPrimaryKey(message);
    }
}
