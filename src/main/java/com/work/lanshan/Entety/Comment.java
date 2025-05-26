package com.work.lanshan.Entety;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Comment {
    private int id;
    private Long article_id;
    private int user_id;
    private String content;
    private Integer parent_id;
    private LocalDateTime create_time;
    private String reply_to_username;  // <--- 新增
    private Integer reply_parent_id;
    private String user_img;

    // 非数据库字段
    private List<Comment> replies; // 用于装子评论
    private String username;       // 评论者用户名（关联 user 表）
}
