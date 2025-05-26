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
          commentMapper.insertComment(comment);
    }

    public Comment findbyId(int id) {
        return commentMapper.findbyId(id);
    }
}
