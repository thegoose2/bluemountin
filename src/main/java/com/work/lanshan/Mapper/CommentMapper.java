package com.work.lanshan.Mapper;

import com.work.lanshan.Entety.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {
    List<Comment> findByArticleId(int articleId);

    List<Comment> findRepliesByParentId(int parentId);

    void insertComment(Comment comment);

    Comment findbyId(int id);

    void updataSupport(int id);
}
