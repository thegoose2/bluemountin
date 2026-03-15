package com.work.lanshan.controller;

import com.work.lanshan.Entety.Messages;
import com.work.lanshan.Entety.Users;
import com.work.lanshan.Mapper.Usermapper;
import com.work.lanshan.config.Messageservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@Controller
public class MessageController {

    @Autowired
    Messageservice messageservice;

    @Autowired
    Usermapper usermapper;

    /**
     * 获取与指定用户的私信列表（片段）
     * @param receiverId 接收者ID
     * @param user 当前用户
     * @param model 模型
     * @return 消息片段
     */
    @GetMapping("/messages/{receiver_id}")
    public String messages(@PathVariable("receiver_id") int receiverId, @AuthenticationPrincipal Users user, Model model) {
        List<Messages> messagesList = messageservice.getMessage(receiverId, user.getId());
        model.addAttribute("messages", messagesList);
        model.addAttribute("user", user);
        return "fragments/messages :: content";
    }

    /**
     * 获取私信对话详情
     * @param receiverId 接收者ID
     * @param user 当前用户
     * @param model 模型
     * @return 消息片段
     */
    @GetMapping("/message/{receiver_id}")
    public String message(@PathVariable("receiver_id") int receiverId, @AuthenticationPrincipal Users user, Model model) {
        List<Messages> messagesList = messageservice.getMessage(receiverId, user.getId());
        Users usertemp = usermapper.findbyid(receiverId);
        model.addAttribute("messages", messagesList);
        model.addAttribute("user", user);
        model.addAttribute("Usertemp", usertemp);
        return "fragments/messages :: content";
    }

    /**
     * 发送私信
     * @param requestMap 请求参数（包含receiverId和content）
     * @param currentUser 当前用户
     * @return 发送结果
     */
    @PostMapping("/messages/send")
    @ResponseBody
    public ResponseEntity<?> sendMessage(@RequestBody Map<String, Object> requestMap,
                                         @AuthenticationPrincipal Users currentUser) {
        try {
            Integer senderId = currentUser.getId();
            Integer receiverId = (Integer) requestMap.get("receiverId");
            String content = (String) requestMap.get("content");

            messageservice.sendMessage(senderId, receiverId, content);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("发送失败");
        }
    }

}
