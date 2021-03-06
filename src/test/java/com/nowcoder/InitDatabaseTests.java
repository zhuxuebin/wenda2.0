package com.nowcoder;

import com.nowcoder.dao.CommentMapper;
import com.nowcoder.dao.LoginTicketMapper;
import com.nowcoder.dao.QuestionMapper;
import com.nowcoder.dao.UserMapper;
import com.nowcoder.domain.*;
import com.nowcoder.service.MessageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by xuery on 2016/8/22.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WendaApplication.class)
//@ContextConfiguration("classpath:spring/spring-dao.xml")
public class InitDatabaseTests {

    @Autowired
    UserMapper userMapper;

    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    LoginTicketMapper loginTicketMapper;

    @Autowired
    CommentMapper commentMapper;

    @Autowired
    MessageService messageService;


    /**
     * 生成要用的数据
     */
    @Test
    public void contextLoads(){
        Random random = new Random();
        for (int i = 0; i < 11; ++i) {
            User user = new User();
            user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
            user.setName(String.format("USER%d", i));
            user.setPassword("");
            user.setSalt("");
            userMapper.insert(user);

            user.setPassword("newpassword");
            userMapper.updateByPrimaryKey(user);

            Question question = new Question();
            question.setCommentCount(i);
            Date date = new Date();
            date.setTime(date.getTime() + 1000 * 3600 * 5 * i);
            question.setCreatedDate(date);
            question.setUserId(i + 1);
            question.setTitle(String.format("TITLE{%d}", i));
            question.setContent(String.format("Balaababalalalal Content %d", i));
            questionMapper.insert(question);
        }
    }

    @Test
    public void testMessageMapper(){
        List<Message> list = messageService.getConversationList(1,0,10);
        List<Message> list1 = messageService.getConversationDetail("1_12",0,10);
        int count = messageService.getConvesationUnreadCount(1,"1_12");
        System.out.println("&*******************"+count);

    }
}
