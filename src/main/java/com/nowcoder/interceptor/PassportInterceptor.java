package com.nowcoder.interceptor;

import com.nowcoder.dao.LoginTicketMapper;
import com.nowcoder.dao.UserMapper;
import com.nowcoder.domain.HostHandler;
import com.nowcoder.domain.LoginTicket;
import com.nowcoder.domain.LoginTicketExample;
import com.nowcoder.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * Created by xuery on 2016/8/23.
 */
@Component
public class PassportInterceptor implements HandlerInterceptor{

    @Autowired
    UserMapper userMapper;

    @Autowired
    LoginTicketMapper loginTicketMapper;


    /**
     * 通过这个对象保证后级Controller可以访问到其中的User
     */
    @Autowired
    HostHandler hostHandler;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String ticket = null;
        if(httpServletRequest.getCookies()!=null){
            for (Cookie cookie : httpServletRequest.getCookies()) {
                if (cookie.getName().equals("ticket")) {
                    ticket = cookie.getValue();
                    break;
                }
            }
        }
        if(ticket!=null){
            LoginTicketExample example = new LoginTicketExample();
            example.createCriteria().andStatusEqualTo(0).andTicketEqualTo(ticket);
            List<LoginTicket> loginList = loginTicketMapper.selectByExample(example);
            if(loginList.size() == 0 || loginList.get(0).getExpired().before(new Date())){
                return true;
            }

            User user = userMapper.selectByPrimaryKey(loginList.get(0).getUserId());
            hostHandler.setUsers(user);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        //跳转到前端页面之前进行相关的拦截操作
        if(modelAndView!=null){
            modelAndView.addObject("user",hostHandler.getUsers());
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        //完成后记得清除
        hostHandler.clear();
    }
}
