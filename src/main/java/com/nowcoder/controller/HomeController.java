package com.nowcoder.controller;

import com.nowcoder.domain.Question;
import com.nowcoder.domain.ViewObject;
import com.nowcoder.service.QuestionService;
import com.nowcoder.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuery on 2016/8/22.
 */
@Controller
public class HomeController {
    public static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    QuestionService questionService;

    @Autowired
    UserService userService;

    @RequestMapping(path={"/","/index"},method = RequestMethod.GET)
    public String index(Model model){
        List<ViewObject> vos = getQuestions(0,0,10);
        model.addAttribute("vos",vos);
        return  "index";
    }

    @RequestMapping(path={"/user/{userId}"},method = RequestMethod.GET)
    public String userIndex(Model model,@PathVariable("userId") int userId){
        List<ViewObject> vos = getQuestions(userId,0,10);
        model.addAttribute("vos",vos);
        return  "index";
    }

    /**
     * 封装成ViewObject 小技巧
     * @param userId
     * @param offset
     * @param limit
     * @return
     */
    private List<ViewObject> getQuestions(int userId,int offset,int limit){
        List<Question> questionList = questionService.getLatestQuestions(userId, offset, limit);
        List<ViewObject> vos = new ArrayList<>(); //相当于List<Map>
        for(Question question:questionList){
            ViewObject vo = new ViewObject();
            vo.set("question",question);
            vo.set("user",userService.getUser(question.getUserId()));
            vos.add(vo);
        }
        return vos;
    }
}
