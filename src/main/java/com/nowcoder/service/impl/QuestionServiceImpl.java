package com.nowcoder.service.impl;

import com.nowcoder.dao.QuestionMapper;
import com.nowcoder.domain.Question;
import com.nowcoder.service.QuestionService;
import com.nowcoder.service.SensitiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * Created by xuery on 2016/8/22.
 */
@Service
public class QuestionServiceImpl implements QuestionService{

    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    SensitiveService sensitiveService;

    @Override
    public List<Question> getLatestQuestions(int userId, int offset, int limit) {
        return questionMapper.selectLatestQuestions(userId,offset,limit);
    }

    @Override
    public Question getById(int id) {
        return questionMapper.selectByPrimaryKey(id);
    }

    @Override
    public int addQuestion(Question question) {
        question.setTitle(HtmlUtils.htmlEscape(question.getTitle()));
        question.setContent(HtmlUtils.htmlEscape(question.getContent()));
        //敏感词过滤
        question.setTitle(sensitiveService.filter(question.getTitle()));
        question.setContent(sensitiveService.filter(question.getContent()));

        return questionMapper.insert(question);
    }

    @Override
    public int updateCommentCount(int id, int count) {
        Question question = questionMapper.selectByPrimaryKey(id);
        question.setCommentCount(count);
        return questionMapper.updateByPrimaryKey(question);
    }
}
