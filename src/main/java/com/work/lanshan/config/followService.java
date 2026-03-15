package com.work.lanshan.config;

import com.work.lanshan.Mapper.user_followMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class followService {
    @Autowired
    user_followMapper followMapper;

    /**
     * 添加关注
     * @param follower_id 关注者ID
     * @param followee_id 被关注者ID
     */
    public void insertfollow(int follower_id,int followee_id){
        followMapper.insertuser_follow(follower_id,followee_id);
    }

    /**
     * 检查是否已关注
     * @param follower_id 关注者ID
     * @param followee_id 被关注者ID
     * @return 是否已关注
     */
    public boolean isFollower(int follower_id, int followee_id){
        return followMapper.isFollower(follower_id,followee_id);
    }

    /**
     * 取消关注
     * @param follower_id 关注者ID
     * @param followee_id 被关注者ID
     */
    public void deletefollow(int follower_id, int followee_id){
        followMapper.deleteuser_follow(follower_id,followee_id);
    }
}
