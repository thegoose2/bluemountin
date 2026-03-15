package com.work.lanshan.Entety;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 评论实体类
 * 对应数据库中的评论表，支持多级回复
 */
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
    private int support_count;

    // 非数据库字段
    private List<Comment> replies; // 用于装子评论
    private String username;       // 评论者用户名（关联 user 表）
}
