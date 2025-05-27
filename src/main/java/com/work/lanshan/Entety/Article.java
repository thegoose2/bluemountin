package com.work.lanshan.Entety;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Article {
    private int id;
    private String title;
    private String content;
    private int author_id;
    private Integer status;  // 0-草稿 1-待审核 2-已发布 3-回收站
    private Integer view_count;
    private Integer like_count;
    private Integer comment_count;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private LocalDateTime published_at;
    private LocalDateTime deleted_at;
    private String author_name;
    private String author_avatar_img;
}
