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

    public List<Comment> getCommentsForArticle(int articleId) {
        List<Comment> topLevelComments = commentMapper.findByArticleId(articleId);
        for (Comment comment : topLevelComments) {
            List<Comment> replies = commentMapper.findRepliesByParentId(comment.getId());
            comment.setReplies(replies);
        }
        return topLevelComments;
    }

    public void addComment(Comment comment) {
        // 如果 parentId 不是 null，要确认是一级评论
        if (comment.getParentId() != null) {
            Comment parent = commentMapper.findRepliesByParentId(comment.getParentId())
                    .stream().findFirst().orElse(null);
            if (parent != null && parent.getParentId() != null) {
                throw new RuntimeException("只允许回复一级评论");
            }
        }
        commentMapper.insertComment(comment);
    }
}
