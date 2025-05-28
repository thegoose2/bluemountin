package com.work.lanshan.Entety;

import lombok.Data;

@Data
public class user_follow {
    private int id;
    private int follower_id; //谁关注的
    private int followee_id; //关注的是谁
}
