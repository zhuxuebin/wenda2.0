package com.nowcoder.dao;

import com.nowcoder.domain.Message;
import com.nowcoder.domain.MessageExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MessageMapper {
    int countByExample(MessageExample example);

    int deleteByExample(MessageExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Message record);

    int insertSelective(Message record);

    List<Message> selectByExampleWithBLOBs(MessageExample example);

    List<Message> selectByExample(MessageExample example);

    Message selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Message record, @Param("example") MessageExample example);

    int updateByExampleWithBLOBs(@Param("record") Message record, @Param("example") MessageExample example);

    int updateByExample(@Param("record") Message record, @Param("example") MessageExample example);

    int updateByPrimaryKeySelective(Message record);

    int updateByPrimaryKeyWithBLOBs(Message record);

    int updateByPrimaryKey(Message record);


    List<Message> getConversationDetail(@Param("conversationId")String conversationId,@Param("offset") int offset, @Param("limit")int limit);

    List<Message> getConversationList(@Param("userId")int userId, @Param("offset") int offset, @Param("limit")int limit);

    int getConvesationUnreadCount(@Param("userId")int userId, @Param("conversationId")String conversationId);

    int getConvesationCount(@Param("conversationId")String conversationId);
}