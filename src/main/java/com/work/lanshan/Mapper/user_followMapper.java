package com.work.lanshan.Mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface user_followMapper {
    /** 添加关注 */
    void insertuser_follow(int follower_id, int followee_id);
    /** 检查是否已关注 */
    boolean isFollower(int follower_id, int followee_id);
    /** 取消关注 */
    void deleteuser_follow(int follower_id, int followee_id);
}
