package com.nowcoder.handler;

import com.nowcoder.async.EventHandler;
import com.nowcoder.async.EventModel;
import com.nowcoder.async.EventType;
import com.nowcoder.util.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xuery on 2016/8/29.
 */
@Component
public class LoginExceptionHandler implements EventHandler{

    @Autowired
    MailSender mailSender;

    /**
     * 当前用户登陆异常
     * 然后在consumerEvent中处理它
     * @param model
     */
    @Override
    public void doHandler(EventModel model) {
        //xxxx判断发现这个用户登陆异常   可以在这里补充代码
        Map<String,Object> map = new HashMap<>();
        map.put("username",model.getExts("username"));
        mailSender.sendWithHTMLTemplate(model.getExts("email"),"登陆ip异常","mails/login_exception.html",map);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LOGIN);
    }
}
