package com.work.lanshan.controller;

import com.work.lanshan.Entety.Users;
import com.work.lanshan.config.followService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/follow")
public class FollowController {

    @Autowired
    private followService followservice;

    /**
     * 关注用户
     * @param followee_id 被关注者ID
     */
    @PostMapping("/{followee_id}")
    public void Followservice(@PathVariable("followee_id") int followee_id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users currentUser = (Users) authentication.getPrincipal();
        int userid = (int) currentUser.getId();
        followservice.insertfollow(userid, followee_id);
    }

    /**
     * 取消关注
     * @param followee_id 被取消关注者ID
     */
    @DeleteMapping("/{followee_id}")
    public void unFollowservice(@PathVariable("followee_id") int followee_id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users currentUser = (Users) authentication.getPrincipal();
        int userid = (int) currentUser.getId();
        followservice.deletefollow(userid, followee_id);
    }
}
