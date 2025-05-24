package com.work.lanshan.controller;

import com.work.lanshan.Components.MarkdownUtils;
import com.work.lanshan.Entety.Article;
import com.work.lanshan.Entety.Users;
import com.work.lanshan.config.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class web {
    private final ArticleService articleService;

    public web(ArticleService articleService) {
        this.articleService = articleService;
    }


    // 首页，显示动态feed内容
    @GetMapping({"/", "/home"})
    public String home(Model model) {
        model.addAttribute("frag", "feed"); // 对应 fragments/feed.html
        return "home";
    }

    // 用户个人主页，显示profile内容
    @GetMapping("/profile")
    public String profile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users currentUser = (Users) authentication.getPrincipal();
        Long userid = (long) currentUser.getId(); // 🎉 直接获取 userId
        List<Article> articleList = articleService.getArticle(userid);
        for (Article aRticle : articleList) {
            String html = MarkdownUtils.markdownToHtml(aRticle.getContent());
            aRticle.setContent(html); // 替换内容
        }
        model.addAttribute("articleList", articleList);
        model.addAttribute("frag", "profile");
        return "home"; // home.html 模板中包含 fragments/profile.html 片段
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

    //注册
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
        model.addAttribute("frags", "login-login");
        return "login";
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

    @GetMapping("/edit")
    public String edit() {
        return "edit";
    }

}
