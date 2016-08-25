package com.nowcoder.controller;

import com.nowcoder.dao.QuestionMapper;
import com.nowcoder.domain.HostHandler;
import com.nowcoder.domain.Question;
import com.nowcoder.service.QuestionService;
import com.nowcoder.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * Created by xuery on 2016/8/23.
 */
@Controller
public class QuestionController {

    private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);

    @Autowired
    QuestionService questionService;

    /**
     * 从Interceptor上传递下来的
     */
    @Autowired
    HostHandler hostHandler;

    @RequestMapping(value="/question/add",method={RequestMethod.POST})
    @ResponseBody
    public String addQuestion(@RequestParam("title") String title,@RequestParam("content") String content){
        try{
            Question question = new Question();
            question.setCommentCount(0);
            question.setContent(content);
            question.setCreatedDate(new Date());
            question.setTitle(title);
            if(hostHandler.getUsers()==null){
                question.setUserId(WendaUtil.ANONYMOUS_USERID);
                //return WendaUtil.getJSONString(999);
            }else{
                question.setUserId(hostHandler.getUsers().getId());
            }
            if(questionService.addQuestion(question) >0){
                return WendaUtil.getJSONString(0);
            }

        }catch(Exception e){
            logger.info("增加题目失败"+e.getMessage());
        }
        return WendaUtil.getJSONString(1,"失败");
    }

    @RequestMapping(value="/question/{qid}",method={RequestMethod.GET})
    public String QuestionDetail(Model model,@PathVariable("qid") int qid){
        Question question = questionService.getById(qid);
        model.addAttribute("question",question);
        return "detail";
    }



}
