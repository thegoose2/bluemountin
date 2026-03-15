package com.work.lanshan.Entety;

import lombok.Data;

/**
 * 收藏实体类
 * 对应数据库中的收藏表，记录用户对文章的收藏关系
 */
@Data
public class favourite {
    int id;
    int user_id;
    int folder_id;
    int article_id;
}
