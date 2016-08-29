package com.nowcoder.controller;

import com.nowcoder.async.EventModel;
import com.nowcoder.async.EventProducer;
import com.nowcoder.async.EventType;
import com.nowcoder.domain.Comment;
import com.nowcoder.domain.EntityType;
import com.nowcoder.domain.HostHolder;
import com.nowcoder.service.CommentService;
import com.nowcoder.service.LikeService;
import com.nowcoder.util.WendaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by xuery on 2016/8/26.
 */
@Controller
public class LikeController {
    @Autowired
    LikeService likeService;

    @Autowired
    CommentService commentService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    EventProducer eventProducer;

    @RequestMapping(value="/like",method = RequestMethod.POST)
    @ResponseBody
    public String like(@RequestParam("commentId") int commentId){
        if(hostHolder.getUsers()==null){
            return WendaUtil.getJSONString(999);
        }
        Comment comment = commentService.selectById(commentId);

        long count = likeService.getLikeCount(EntityType.ENTITY_COMMENT,commentId);
        long likeCount = likeService.like(hostHolder.getUsers().getId(),EntityType.ENTITY_COMMENT,commentId);
        if(count == likeCount){
            //说明当前用户已经点过赞了，因为加入集合中，集合个数没有变化
        }else{
            //异步点赞之后给评论发表者发站内信   防止用户点赞后一直点还继续发站内信，这样不科学
            eventProducer.fireEvent(new EventModel(EventType.LIKE)
                    .setActorId(hostHolder.getUsers().getId()).setEntityId(commentId)
                    .setEntityOwnerId(comment.getUserId())
                    .setEntityType(EntityType.ENTITY_COMMENT)
                    .setExts("questionId",String.valueOf(comment.getEntityId())));
        }
        return WendaUtil.getJSONString(0,String.valueOf(likeCount));
    }

    @RequestMapping(value="/dislike",method = RequestMethod.POST)
    @ResponseBody
    public String dislike(@RequestParam("commentId") int commentId){
        if(hostHolder.getUsers()==null){
            return WendaUtil.getJSONString(999);
        }
        Comment comment = commentService.selectById(commentId);

        long likeCount = likeService.disLike(hostHolder.getUsers().getId(),EntityType.ENTITY_COMMENT,commentId);
        return WendaUtil.getJSONString(0,String.valueOf(likeCount));
    }



}
