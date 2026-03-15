package com.work.lanshan.Entety;

import lombok.Data;

import java.util.List;

/**
 * 文件夹实体类
 * 对应数据库中的文件夹表，用于用户收藏文章的分类
 */
@Data
public class folder {
    private int id;
    private String name;
    private int user_id;
    private List<Article> totalArticles;
}
