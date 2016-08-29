package com.nowcoder.handler;

import com.nowcoder.async.EventHandler;
import com.nowcoder.async.EventModel;
import com.nowcoder.async.EventType;
import com.nowcoder.domain.Message;
import com.nowcoder.domain.User;
import com.nowcoder.service.MessageService;
import com.nowcoder.service.UserService;
import com.nowcoder.util.WendaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by xuery on 2016/8/27.
 */
@Component
public class LikeHandler implements EventHandler {

    @Autowired
    UserService userService;

    @Autowired
    MessageService messageService;


    @Override
    public void doHandler(EventModel model) {
        Message message = new Message();
        message.setFromId(WendaUtil.SYSTEM_USERID);
        message.setToId(model.getEntityOwnerId());
        message.setCreatedDate(new Date());
        message.setHasRead(0);
        if(WendaUtil.SYSTEM_USERID < model.getEntityOwnerId()) {
            message.setConversationId(WendaUtil.SYSTEM_USERID + "_" + model.getEntityOwnerId());
        }
        else {
            message.setConversationId(model.getEntityOwnerId() + "_" + WendaUtil.SYSTEM_USERID);
        }
        User user = userService.getUser(model.getActorId());
        message.setContent("用户"+user.getName()+"赞了你的评论，http://127.0.0.1:8080/question/"+model.getExts("questionId"));
        messageService.addMessage(message);

    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);
    }
}
