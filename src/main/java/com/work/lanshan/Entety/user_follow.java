package com.work.lanshan.Entety;

import lombok.Data;

/**
 * 用户关注关系实体类
 * 对应数据库中的关注表，记录用户之间的关注关系
 */
@Data
public class user_follow {
    private int id;
    private int follower_id; //谁关注的
    private int followee_id; //关注的是谁
}
