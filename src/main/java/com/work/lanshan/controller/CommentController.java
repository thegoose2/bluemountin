package com.work.lanshan.controller;

import com.work.lanshan.Entety.Comment;
import com.work.lanshan.config.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping("/comment")
    public String postComment(@ModelAttribute Comment comment) {
        commentService.addComment(comment);
        return "redirect:/article/" + comment.getArticleId();
    }
}
