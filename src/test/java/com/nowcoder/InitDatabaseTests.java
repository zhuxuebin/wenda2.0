package com.nowcoder;

import com.nowcoder.dao.CommentMapper;
import com.nowcoder.dao.LoginTicketMapper;
import com.nowcoder.dao.QuestionMapper;
import com.nowcoder.dao.UserMapper;
import com.nowcoder.domain.*;
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
    public void testLoginTickeMapper(){
//        String ticket = "1213323";
//        LoginTicketExample example = new LoginTicketExample();
//        example.createCriteria().andStatusEqualTo(0).andTicketEqualTo(ticket);
//        List<LoginTicket> list = loginTicketMapper.selectByExample(example);

//        UserExample userExample = new UserExample();
//        userExample.createCriteria().andNameEqualTo("USER0");
//        List<User> users = userMapper.selectByExample(userExample);


        //List<Question > list = questionMapper.selectLatestQuestions(0,0,10);
        //List<Comment>  comment = commentMapper.getCommentsByEntity(12,1);


        commentMapper.deleteComment(12,1);

        commentMapper.getCommentCount(12,1);



        //int x = commentMapper.getCommentCount(12,1);


        System.out.println("&*******************");

    }
}
