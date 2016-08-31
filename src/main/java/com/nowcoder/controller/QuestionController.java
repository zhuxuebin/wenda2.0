package com.nowcoder.controller;

import com.nowcoder.domain.*;
import com.nowcoder.service.*;
import com.nowcoder.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by xuery on 2016/8/23.
 */
@Controller
public class QuestionController {

    private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);

    @Autowired
    QuestionService questionService;

    @Autowired
    CommentService commentService;

    @Autowired
    UserService userService;

    @Autowired
    LikeService likeService;

    @Autowired
    FollowService followService;

    /**
     * 从Interceptor上传递下来的
     */
    @Autowired
    HostHolder hostHolder;

    @RequestMapping(value="/question/add",method={RequestMethod.POST})
    @ResponseBody
    public String addQuestion(@RequestParam("title") String title,@RequestParam("content") String content){
        try{
            Question question = new Question();
            question.setCommentCount(0);
            question.setContent(content);
            question.setCreatedDate(new Date());
            question.setTitle(title);
            if(hostHolder.getUsers()==null){
                question.setUserId(WendaUtil.ANONYMOUS_USERID);
                //return WendaUtil.getJSONString(999);
            }else{
                question.setUserId(hostHolder.getUsers().getId());
            }
            if(questionService.addQuestion(question) >0){
                return WendaUtil.getJSONString(0);
            }

        }catch(Exception e){
            logger.info("增加题目失败"+e.getMessage());
        }
        return WendaUtil.getJSONString(1,"失败");
    }


    /**
     * 展示某条问题
     * 并显示与其相关的评论
     * @param model
     * @param qid
     * @return
     */
    @RequestMapping(value="/question/{qid}",method={RequestMethod.GET})
    public String QuestionDetail(Model model,@PathVariable("qid") int qid){
        Question question = questionService.getById(qid);
        model.addAttribute("question",question);

        //查询与当前问题相关的评论
        List<Comment> commmentList = commentService.getCommentsByEntity(qid, EntityType.ENTITY_QUESTION);
        List<ViewObject> vos = new ArrayList<>(); //为什么要，因为每条评论可能对应不同的用户，所以需要将评论和用户一一对应
        for(Comment comment:commmentList){
            ViewObject vo = new ViewObject();
            if(hostHolder.getUsers() == null){
                vo.set("liked",0);
            }else{
                vo.set("liked",likeService.getLikeStatus(hostHolder.getUsers().getId(),
                        EntityType.ENTITY_COMMENT,comment.getId()));

            }
            vo.set("likeCount",likeService.getLikeCount(EntityType.ENTITY_COMMENT,comment.getId()));
            vo.set("user",userService.getUser(comment.getUserId()));
            vo.set("comment",comment);
            vos.add(vo);
        }
        model.addAttribute("comments",vos);

        List<ViewObject> followUsers = new ArrayList<>();
        //获取关注用户的信息
        List<Integer> users = followService.getFollowers(EntityType.ENTITY_QUESTION,qid,20);
        for(Integer userId :users){
            ViewObject vo = new ViewObject();
            User u = userService.getUser(userId);
            if(u == null){
                continue;
            }
            vo.set("name",u.getName());
            vo.set("headUrl",u.getHeadUrl());
            vo.set("id",u.getId());
            followUsers.add(vo);
        }
        model.addAttribute("followUsers",followUsers);
        if(hostHolder.getUsers()!=null){
            model.addAttribute("followed",followService.isFollower(hostHolder.getUsers().getId(),EntityType.ENTITY_QUESTION,
                    qid));
        }else{
            model.addAttribute("followed",false);
        }

        return "detail";
    }



}
