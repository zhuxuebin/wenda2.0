package com.nowcoder.controller;

import com.nowcoder.domain.Comment;
import com.nowcoder.domain.EntityType;
import com.nowcoder.domain.HostHandler;
import com.nowcoder.service.CommentService;
import com.nowcoder.service.QuestionService;
import com.nowcoder.service.SensitiveService;
import com.nowcoder.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.HtmlUtils;

import java.util.Date;

/**
 * Comment是和Question相关的，所以Question那边的一些业务操作也需要进行相应的修改
 *
 * 以后扩展的时候可能不止question，还有比如编程题发布答案
 *
 * 所以设计数据库时才需要设计entityId entityType
 * Created by xuery on 2016/8/25.
 */
@Controller
public class CommentController {

    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    CommentService commentService;

    @Autowired
    SensitiveService sensitiveService;

    @Autowired
    HostHandler hostHandler;

    @Autowired
    QuestionService questionService;

    @RequestMapping(value="/addComment",method = RequestMethod.POST)
    public String addComment(@RequestParam("questionId") int questionId,
                             @RequestParam("content") String content){
        try{
            //先过滤下
            content = HtmlUtils.htmlEscape(content);
            content = sensitiveService.filter(content);
            Comment comment = new Comment();
            if(hostHandler.getUsers()==null){
                comment.setUserId(WendaUtil.ANONYMOUS_USERID);
            }else{
                comment.setUserId(hostHandler.getUsers().getId());
            }
            comment.setContent(content);
            comment.setCreatedDate(new Date());
            comment.setEntityId(questionId);
            comment.setEntityType(EntityType.ENTITY_QUESTION);
            comment.setStatus(0);

            commentService.addComment(comment);
            //每次插入完   更新题目里的评论数量  相应的更新question表   z之后需要异步化
            int count = commentService.getCommentCount(questionId,EntityType.ENTITY_QUESTION);
            questionService.updateCommentCount(questionId,count);

        }catch(Exception e){
            logger.info("添加评论失败:"+e.getMessage());
        }

        return "redirect:/question/"+String.valueOf(questionId);  //评论完后跳到评论的问题并更新评论
    }





}
