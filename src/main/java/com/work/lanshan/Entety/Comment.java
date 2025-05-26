package com.work.lanshan.Entety;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Comment {
    private int id;
    private Long articleId;
    private Long userId;
    private String content;
    private Integer parentId;
    private LocalDateTime createTime;

    // 非数据库字段
    private List<Comment> replies; // 用于装子评论
    private String username;       // 评论者用户名（关联 user 表）
}
