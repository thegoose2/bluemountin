package com.work.lanshan.config;

import com.work.lanshan.Entety.Comment;
import com.work.lanshan.Mapper.CommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CommentService {
    @Autowired
    private CommentMapper commentMapper;

    /**
     * 获取文章的所有评论（包括一级评论和回复）
     * @param articleId 文章ID
     * @return 评论列表（包含嵌套的回复）
     */
    public List<Comment> getCommentsForArticle(int articleId) {
        List<Comment> topLevelComments = commentMapper.findByArticleId(articleId);
        for (Comment comment : topLevelComments) {
            List<Comment> replies = commentMapper.findRepliesByParentId(comment.getId());
            comment.setReplies(replies);
        }
        return topLevelComments;
    }

    /**
     * 添加评论
     * @param comment 评论对象
     */
    public void addComment(Comment comment) {
          commentMapper.insertComment(comment);
    }

    /**
     * 根据ID查找评论
     * @param id 评论ID
     * @return 评论对象
     */
    public Comment findbyId(int id) {
        return commentMapper.findbyId(id);
    }
}
