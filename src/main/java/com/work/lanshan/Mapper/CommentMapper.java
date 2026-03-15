package com.work.lanshan.Mapper;

import com.work.lanshan.Entety.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {
    /** 根据文章ID查询一级评论 */
    List<Comment> findByArticleId(int articleId);

    /** 根据父评论ID查询回复 */
    List<Comment> findRepliesByParentId(int parentId);

    /** 插入评论 */
    void insertComment(Comment comment);

    /** 根据ID查询评论 */
    Comment findbyId(int id);

    /** 增加评论点赞数 */
    void updataSupport(int id);
}
