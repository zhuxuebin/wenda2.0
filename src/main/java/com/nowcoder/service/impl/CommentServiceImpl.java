package com.nowcoder.service.impl;

import com.nowcoder.dao.CommentMapper;
import com.nowcoder.domain.Comment;
import com.nowcoder.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by xuery on 2016/8/25.
 */
@Service
public class CommentServiceImpl implements CommentService{

    @Autowired
    CommentMapper commentMapper;

    @Override
    public List<Comment> getCommentsByEntity(int entityId, int entityType) {
        return commentMapper.getCommentsByEntity(entityId,entityType);
    }

    @Override
    public int addComment(Comment comment) {
        return commentMapper.insert(comment);
    }

    @Override
    public int getCommentCount(int entityId, int entityType) {
        return commentMapper.getCommentCount(entityId,entityType);
    }

    @Override
    public void deleteComment(int entityId, int entityType) {
       commentMapper.deleteComment(entityId,entityType);
    }
}
