package com.nowcoder.service.impl;

import com.nowcoder.dao.LoginTicketMapper;
import com.nowcoder.dao.UserMapper;
import com.nowcoder.domain.LoginTicket;
import com.nowcoder.domain.LoginTicketExample;
import com.nowcoder.domain.User;
import com.nowcoder.domain.UserExample;
import com.nowcoder.service.UserService;
import com.nowcoder.util.WendaUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by xuery on 2016/8/22.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    LoginTicketMapper loginTicketMapper;

    @Override
    public User getUser(int id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public Map<String, Object> register(String username,String password){
        Map<String,Object> map = new HashMap<>();
        if(StringUtils.isBlank(username)){
            map.put("msg","用户名不能为空");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("msg","密码不能为空");
            return map;
        }

        //规定用户名唯一
        UserExample example = new UserExample();
        example.createCriteria().andNameEqualTo(username);
        List<User> userList = userMapper.selectByExample(example);
        if(userList.size()!=0){
            map.put("msg","用户名已经被注册");
            return map;
        }

        //注册成功将注册信息加密后写入数据库
        User user = new User();
        user.setName(username);
        user.setSalt(UUID.randomUUID().toString().substring(0, 5));
        String headUrl = String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000));
        user.setHeadUrl(headUrl);
        user.setPassword(WendaUtil.MD5(user.getSalt() + password));
        userMapper.insert(user);

        String ticket = addLoginTicket(user.getId());
        map.put("ticket",ticket);

        return map;
    }

    @Override
    public Map<String,Object> login(String username,String password){
        Map<String,Object> map = new HashMap<>();
        if(StringUtils.isBlank(username)){
            map.put("msg","用户名不能为空");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("msg","密码不能为空");
            return map;
        }

        //规定用户名唯一
        UserExample example = new UserExample();
        example.createCriteria().andNameEqualTo(username);
        List<User> userList = userMapper.selectByExample(example);
        if(userList.size()!=0){
            User user = userList.get(0);
            String pass = WendaUtil.MD5(user.getSalt() + password);
            if(!pass.equals(user.getPassword())){
                map.put("msg","密码错误，请重新输入");
                return map;
            }
        }else{
            map.put("msg","用户名不存在");
            return map;
        }

        LoginTicketExample loginTicketExample = new LoginTicketExample();
        loginTicketExample.createCriteria().andStatusEqualTo(0).andUserIdEqualTo(userList.get(0).getId());
        List<LoginTicket> loginList = loginTicketMapper.selectByExample(loginTicketExample);
        String ticket = "";
        if(loginList.size() >0){
            //更新
            LoginTicket loginTicket = loginList.get(0);
            ticket = updateLoginTicket(loginTicket);
        }else{
            //插入
            ticket = addLoginTicket(userList.get(0).getId());
        }
        map.put("ticket",ticket);
        return map;
    }

    private String addLoginTicket(int userid){
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(userid);
        Date date = new Date();
        date.setTime(date.getTime()+3600*24*5);
        loginTicket.setExpired(date);
        loginTicket.setStatus(0);
        loginTicket.setTicket(UUID.randomUUID().toString().replaceAll("-",""));
        loginTicketMapper.insert(loginTicket);


        return loginTicket.getTicket();
    }

    private String updateLoginTicket(LoginTicket loginTicket){
        Date date = new Date();
        date.setTime(date.getTime()+3600*24*5);
        loginTicket.setExpired(date);
        loginTicketMapper.updateByPrimaryKey(loginTicket);

        return loginTicket.getTicket();
    }

    @Override
    public  void logout(String ticket){
        LoginTicket loginTicket = new LoginTicket();
        LoginTicketExample example = new LoginTicketExample();
        example.createCriteria().andTicketEqualTo(ticket);
        List<LoginTicket> loginList = loginTicketMapper.selectByExample(example);
        if(loginList.size() > 0){
            loginTicket = loginList.get(0);
            loginTicket.setStatus(1);
            loginTicketMapper.updateByPrimaryKey(loginTicket);
        }
    }

    @Override
    public User selectUserByOneExample(UserExample userExample){
        List<User> list = userMapper.selectByExample(userExample);
        if(list.size()!=0){
            return list.get(0);
        }
        return null;
    }
}
