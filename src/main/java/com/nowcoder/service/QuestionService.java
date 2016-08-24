package com.nowcoder.service;

import com.nowcoder.domain.Question;

import java.util.List;

/**
 * Created by xuery on 2016/8/22.
 */
public interface QuestionService {

    List<Question> getLatestQuestions(int userId, int offset, int limit);

    Question getById(int id);

    int addQuestion(Question question);

    int updateCommentCount(int id, int count);
}
