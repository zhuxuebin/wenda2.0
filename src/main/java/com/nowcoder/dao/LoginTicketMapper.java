package com.nowcoder.dao;

import com.nowcoder.domain.LoginTicket;
import com.nowcoder.domain.LoginTicketExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LoginTicketMapper {
    int countByExample(LoginTicketExample example);

    int deleteByExample(LoginTicketExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(LoginTicket record);

    int insertSelective(LoginTicket record);

    List<LoginTicket> selectByExample(LoginTicketExample example);

    LoginTicket selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") LoginTicket record, @Param("example") LoginTicketExample example);

    int updateByExample(@Param("record") LoginTicket record, @Param("example") LoginTicketExample example);

    int updateByPrimaryKeySelective(LoginTicket record);

    int updateByPrimaryKey(LoginTicket record);
}