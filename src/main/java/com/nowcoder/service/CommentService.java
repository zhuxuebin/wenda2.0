package com.nowcoder.service;

import com.nowcoder.domain.Comment;

import java.util.List;

/**
 * Created by xuery on 2016/8/25.
 */
public interface CommentService {
    /**
     * 获取某个表的某个id的评论
     * @param entityId
     * @param entityType
     * @return
     */
     List<Comment> getCommentsByEntity(int entityId, int entityType);

    /**
     * 新增评论
     * @param comment
     * @return
     */
     int addComment(Comment comment);

    /**
     * 获取某个问题的评论数
     * @param entityId
     * @param entityType
     * @return
     */
     int getCommentCount(int entityId, int entityType);


    /**
     * 删除评论,这里写的应该是对于一个问题的所有评论都删除，而不是删除某一条评论
     * @param entityId
     * @param entityType
     */
     void deleteComment(int entityId, int entityType);

}
