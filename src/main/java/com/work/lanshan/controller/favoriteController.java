package com.work.lanshan.controller;

import com.work.lanshan.Entety.Users;
import com.work.lanshan.config.favoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class favoriteController {
    @Autowired
    private favoriteService favouriteservice;

    @PostMapping("/favorite/add")
    @ResponseBody
    public ResponseEntity<?> addFavorite(@RequestBody Map<String, Integer> request,
                                         @AuthenticationPrincipal Users user) {
        try {
            Integer articleId = request.get("articleId");
            Integer folderId = request.get("folderId");
            favouriteservice.insertfavorite(user.getId(),folderId,articleId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("参数错误");
        }
    }
}
