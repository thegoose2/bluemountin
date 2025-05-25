package com.work.lanshan.controller;

import com.work.lanshan.Components.MarkdownUtils;
import com.work.lanshan.Entety.Article;
import com.work.lanshan.Entety.Role;
import com.work.lanshan.Entety.Users;
import com.work.lanshan.config.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

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
        int userid = (int) currentUser.getId(); // 🎉 直接获取 userId
        boolean T=false;
        List<Role> roles = usermapper.getUserRolesByUid(userid);
        for (Role role : roles) {
            if(role.getName().equals("ROLE_ADMIN")){
                T=true;
            }
        }
        List<Article> articleList = articleService.getArticle(userid,1);
        for (Article aRticle : articleList) {
            String html = MarkdownUtils.markdownToHtml(aRticle.getContent());
            aRticle.setContent(html); // 替换内容
        }

        Users user = usermapper.findbyusername(currentUser.getUsername());
        model.addAttribute("user", user);
        model.addAttribute("articleList", articleList);
        model.addAttribute("frag", "profile");
        model.addAttribute("TT", T);
        return "home"; // home.html 模板中包含 fragments/profile.html 片段
    }

    // 登录页
    @GetMapping("/login")
    public String login() {
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
        model.addAttribute("frag", "login-registered");
        return "login";
    }

    @GetMapping("/registerer/fragment")
    public String registereded() {
        return "fragments/login-registered :: login-content";
    }

    @GetMapping("/login/fragment")
    public String logined() {
        return "fragments/login-login :: login-content";
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

    @PostMapping("/users/uploadAvatar")
    public String uploadAvatar(@RequestParam("avatar") MultipartFile file,Model model) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users currentUser = (Users) authentication.getPrincipal();
        int userid = (int) currentUser.getId(); // 🎉 直接获取 userId

        String uploadDir = "E:/code/JAVA/pic/";
        File dir = new File(uploadDir);
        if(!dir.exists()) dir.mkdirs();

        String ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String fileName = UUID.randomUUID().toString() + ext;
        File dest = new File(uploadDir + fileName);
        file.transferTo(dest);

        boolean T=false;
        List<Role> roles = usermapper.getUserRolesByUid(userid);
        for (Role role : roles) {
            if(role.getName().equals("ROLE_ADMIN")){
                T=true;
            }
        }
        List<Article> articleList = articleService.getArticle(userid,1);
        for (Article aRticle : articleList) {
            String html = MarkdownUtils.markdownToHtml(aRticle.getContent());
            aRticle.setContent(html); // 替换内容
        }

        String avatarUrl = "/uploads/" + fileName;
        usermapper.updateAvatar(userid,avatarUrl);
        Users user = usermapper.findbyusername(currentUser.getUsername());
        model.addAttribute("articleList", articleList);
        model.addAttribute("frag", "profile");
        model.addAttribute("user", user);
        model.addAttribute("TT", T);
        return "home";

    }

    @GetMapping("/myblog")
    public String blog(@RequestParam(defaultValue = "1") int temp, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users currentUser = (Users) authentication.getPrincipal();
        int userid = (int) currentUser.getId(); // 🎉 直接获取 userId

        boolean T=false;
        List<Role> roles = usermapper.getUserRolesByUid(userid);
        for (Role role : roles) {
            if(role.getName().equals("ROLE_ADMIN")){
                T=true;
            }
        }
        List<Article> articleList = articleService.getArticle(userid,temp);
        for (Article aRticle : articleList) {
            String html = MarkdownUtils.markdownToHtml(aRticle.getContent());
            aRticle.setContent(html); // 替换内容
        }

        Users user = usermapper.findbyusername(currentUser.getUsername());
        model.addAttribute("articleList", articleList);
        model.addAttribute("frag", "profile");
        model.addAttribute("user", user);
        model.addAttribute("TT", T);
        return "home";
    }

    @GetMapping("/profile/fragment")
    public String profileFragment(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users currentUser = (Users) authentication.getPrincipal();
        int userid = (int) currentUser.getId();
        boolean T = false;
        List<Role> roles = usermapper.getUserRolesByUid(userid);
        for (Role role : roles) {
            if (role.getName().equals("ROLE_ADMIN")) {
                T = true;
            }
        }
        List<Article> articleList = articleService.getArticle(userid, 1);
        for (Article aRticle : articleList) {
            String html = MarkdownUtils.markdownToHtml(aRticle.getContent());
            aRticle.setContent(html);
        }

        Users user = usermapper.findbyusername(currentUser.getUsername());
        model.addAttribute("user", user);
        model.addAttribute("articleList", articleList);
        model.addAttribute("TT", T);
        return "fragments/profile :: content"; // 只返回中间部分
    }

    @GetMapping("/gate/fragment")
    public String gateFragment(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users currentUser = (Users) authentication.getPrincipal();
        int userid = (int) currentUser.getId();
        boolean T = false;
        List<Role> roles = usermapper.getUserRolesByUid(userid);
        for (Role role : roles) {
            if (role.getName().equals("ROLE_ADMIN")) {
                T = true;
            }
        }
        List<Article> articleList = articleService.getArticle(userid, 1);
        for (Article aRticle : articleList) {
            String html = MarkdownUtils.markdownToHtml(aRticle.getContent());
            aRticle.setContent(html);
        }

        Users user = usermapper.findbyusername(currentUser.getUsername());
        model.addAttribute("user", user);
        model.addAttribute("articleList", articleList);
        model.addAttribute("TT", T);
        return "fragments/feed :: content"; // 只返回中间部分
    }

    @GetMapping("/article/{id}")
    public String showArticleDetail(@PathVariable int id, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users currentUser = (Users) authentication.getPrincipal();
        Article article = articleService.findarticle(id); // 获取文章及评论
        String html = MarkdownUtils.markdownToHtml(article.getContent());
        article.setContent(html);
        model.addAttribute("user", currentUser);
        model.addAttribute("article", article);
        return "article-detail"; // 对应 templates/article-detail.html
    }



}
