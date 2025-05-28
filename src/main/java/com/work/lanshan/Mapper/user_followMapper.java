package com.work.lanshan.Mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface user_followMapper {
    void insertuser_follow(int follower_id, int followee_id);
    boolean isFollower(int follower_id, int followee_id);
    void deleteuser_follow(int follower_id, int followee_id);
}
