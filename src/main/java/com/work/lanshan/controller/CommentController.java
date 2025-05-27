package com.work.lanshan.controller;

import com.work.lanshan.Entety.Comment;
import com.work.lanshan.Entety.Users;
import com.work.lanshan.Mapper.CommentMapper;
import com.work.lanshan.config.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class CommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private CommentMapper commentMapper;

    @PostMapping("/comment")
    public String postComment(@ModelAttribute Comment comment) {
        // 如果这条评论是对别人的回复（parentId 不为空）
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users currentUser = (Users) authentication.getPrincipal();
        int userid = (int) currentUser.getId();
        comment.setUsername(currentUser.getUsername());
        comment.setUser_id(userid);
        if (comment.getReply_parent_id() != null) {
            // 查出被回复的评论
            Comment parent = commentService.findbyId(comment.getReply_parent_id());
            // 如果被回复的评论本身也是个回复（即是“二级评论”），我们就加上 replyToUsername
            if (parent.getParent_id() != null) {
                comment.setReply_to_username(parent.getUsername());
            }
        }

        // 保存评论
        comment.setUser_img(currentUser.getImg());
        commentService.addComment(comment);

        // 重定向回文章页
        return "redirect:/article/" + comment.getArticle_id();
    }

    //点赞机制
    @PostMapping("/like")
    @ResponseBody
    public Map<String, Object> postLike(@RequestParam("comment_id") int id) {
        commentMapper.updataSupport(id);
        Comment comment = commentMapper.findbyId(id);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("support_count", comment.getSupport_count());
        return response;
    }

}
