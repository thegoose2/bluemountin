package com.work.lanshan.controller;

import com.work.lanshan.Entety.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class web {

    // 首页，显示动态feed内容
    @GetMapping({"/", "/home"})
    public String home(Model model) {
        model.addAttribute("frag", "feed"); // 对应 fragments/feed.html
        return "home";
    }

    // 用户个人主页，显示profile内容
    @GetMapping("/profile")
    public String profile(Model model) {
        model.addAttribute("frag", "profile"); // 对应 fragments/profile.html
        return "home"; // 用同一个 home 模板
    }

    // 登录页
    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("frags", "login-login");
        return "login";
    }

    @GetMapping("/loginerror")
    public String loginerror(Model model) {
        model.addAttribute("frags", "login-login");
        model.addAttribute("error", "用户或密码错误！");
        return "login";
    }

    @GetMapping("/registerer")
    public String registered(Model model) {
        model.addAttribute("frags", "login-registered");
        return "login";
    }

    //后台登录与注册
    @Autowired
    private com.work.lanshan.Mapper.Usermapper usermapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/login/register")
    public String toregister(Users user, Model model, @RequestParam String confirmpassword) {
        if(!user.getPassword().equals(confirmpassword)) {
            model.addAttribute("error", "两次密码不一致！");
            model.addAttribute("frags", "login-registered");
            return "login";
        }

        if(usermapper.findbyusername(user.getUsername()) != null){
            model.addAttribute("error", "用户名已存在！");
            model.addAttribute("frags", "login-registered");
            return "login";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        usermapper.insertuser(user);
        usermapper.setuserrole(usermapper.findbyusername(user.getUsername()).getId(),1);
        model.addAttribute("frag", "profile");
        return "home";
    }
//自定义登录控制器
//    @PostMapping("/login/login")
//    public String tologin(Users user, Model model) {
//        System.out.println(usermapper.findbyusername(user.getUsername()));
//        if(usermapper.findbyusername(user.getUsername()) == null) {
//            System.out.println("2");
//            model.addAttribute("error","用户不存在！");
//            model.addAttribute("frags", "login-login");
//            return "login";
//        }
//        else{
//            if(passwordEncoder.matches(user.getPassword(), usermapper.findbyusername(user.getUsername()).getPassword())) {
//                model.addAttribute("frag","profile");
//                model.addAttribute("username", user.getUsername());
//                return "home";
//            }
//            else{
//                model.addAttribute("frags", "login-login");
//                model.addAttribute("error","密码错误！");
//                return "login";
//            }
//        }
//    }


}
