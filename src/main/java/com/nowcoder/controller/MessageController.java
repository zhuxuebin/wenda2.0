package com.nowcoder.controller;

import com.nowcoder.domain.*;
import com.nowcoder.service.MessageService;
import com.nowcoder.service.UserService;
import com.nowcoder.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by xuery on 2016/8/26.
 */
@Controller
public class MessageController {

    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Autowired
    HostHolder hostHolder;

    /**
     * 只有登录了才会显示私信按钮，所以不用拦截用户登陆
     * 获取当前用户的站内信列表
     * @param model
     * @return
     */
    @RequestMapping(value="/msg/list",method={RequestMethod.GET})
    public String conversationDetail(Model model){
        try{
            int localUserId = hostHolder.getUsers().getId();
            List<Message> messageList = messageService.getConversationList(localUserId,0,10);
            List<ViewObject> conversations = new ArrayList<>();
            for(Message message : messageList){
                ViewObject vo = new ViewObject();
                int targetId = message.getFromId() == localUserId?message.getToId():message.getFromId();
                User user = userService.getUser(targetId); //接收者
                vo.set("user",user);
                vo.set("message",message);
                vo.set("unread",messageService.getConvesationUnreadCount(localUserId,message.getConversationId()));
                conversations.add(vo);
            }
            model.addAttribute("conversations",conversations);
        }catch(Exception e){
            logger.info("获取站内信列表出错:"+e.getMessage());
        }
        return "letter";
    }


    @RequestMapping(value="/msg/detail",method=RequestMethod.GET)
    public String conversationDetail(Model model,@RequestParam("conversationId")String conversationId){
        try{
            List<Message> list = messageService.getConversationDetail(conversationId,0,10);
            List<ViewObject> messages = new ArrayList<>();
            for(Message msg:list){
                //先对每一条message置为已读
                messageService.updateMessageToRead(msg);

                ViewObject vo = new ViewObject();
                vo.set("message",msg);
                User user = userService.getUser(msg.getFromId());
                if(user == null){
                    continue;
                }
                vo.set("headUrl",user.getHeadUrl());
                vo.set("username",user.getName());
                vo.set("user",user);
                messages.add(vo);
            }
            model.addAttribute("messages",messages);
        }catch(Exception e){
            logger.info("获取详情消息失败:"+e.getMessage());
        }
        return "letterDetail";
    }

    @RequestMapping(path = {"/msg/addMessage"}, method = {RequestMethod.POST})
    @ResponseBody
    public String addMessage(@RequestParam("toName") String toName,
                             @RequestParam("content") String content) {
        try {
            if (hostHolder.getUsers() == null) {
                return WendaUtil.getJSONString(999, "未登录");
            }
            UserExample example = new UserExample();
            example.createCriteria().andNameEqualTo(toName);
            User user = userService.selectUserByOneExample(example);
            if (user == null) {
                return WendaUtil.getJSONString(1, "用户不存在");
            }

            Message message = new Message();
            message.setCreatedDate(new Date());
            message.setFromId(hostHolder.getUsers().getId());
            message.setToId(user.getId());
            message.setContent(content);
            if(message.getFromId() < message.getToId()){
                message.setConversationId(message.getFromId()+"_"+message.getToId());
            }else{
                message.setConversationId(+message.getToId()+"_"+message.getFromId());
            }
            messageService.addMessage(message);
            return WendaUtil.getJSONString(0);

        } catch (Exception e) {
            logger.error("发送消息失败" + e.getMessage());
            return WendaUtil.getJSONString(1, "发信失败");
        }
    }
}
