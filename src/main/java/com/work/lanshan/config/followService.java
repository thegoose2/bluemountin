package com.work.lanshan.config;

import com.work.lanshan.Mapper.user_followMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class followService {
    @Autowired
    user_followMapper followMapper;

    public void insertfollow(int follower_id,int followee_id){
        followMapper.insertuser_follow(follower_id,followee_id);
    }

    public boolean isFollower(int follower_id, int followee_id){
        return followMapper.isFollower(follower_id,followee_id);
    }

    public void deletefollow(int follower_id, int followee_id){
        followMapper.deleteuser_follow(follower_id,followee_id);
    }
}
