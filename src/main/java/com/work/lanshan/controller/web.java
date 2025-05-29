package com.work.lanshan.controller;

import com.work.lanshan.Components.MarkdownUtils;
import com.work.lanshan.Entety.*;
import com.work.lanshan.config.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
public class web {
    private final ArticleService articleService;

    public web(ArticleService articleService) {
        this.articleService = articleService;
    }


    @Autowired
    private CommentService commentService;

    @Autowired
    private followService followService;

    @Autowired
    private favoriteService favoriteservice;

    @Autowired
    private Messageservice messageservice;


    // 首页，显示动态feed内容
    @GetMapping({"/", "/home"})
    public String home(Model model) {
        List<Article> articleList = articleService.selectAll(1);
        for (Article aRticle : articleList) {
            String html = MarkdownUtils.markdownToHtml(aRticle.getContent());
            aRticle.setContent(html); // 替换内容
        }
        model.addAttribute("articleList", articleList);
        model.addAttribute("frag", "feed");
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
            if (role.getName().equals("ROLE_ADMIN")) {
                T = true;
                break;
            }
        }
        List<Article> articleList = articleService.getArticle(userid,1);
        for (Article aRticle : articleList) {
            String html = MarkdownUtils.markdownToHtml(aRticle.getContent());
            aRticle.setContent(html);
            articleService.updatalike(aRticle.getId());
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
    public String login(Model model) {
        model.addAttribute("frag", "login-login");
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
            articleService.updatalike(aRticle.getId());
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
            if (role.getName().equals("ROLE_ADMIN")) {
                T = true;
                break;
            }
        }
        List<Article> articleList = articleService.getArticle(userid,temp);
        for (Article aRticle : articleList) {
            String html = MarkdownUtils.markdownToHtml(aRticle.getContent());
            aRticle.setContent(html); // 替换内容
            articleService.updatalike(aRticle.getId());
        }

        Users user = usermapper.findbyusername(currentUser.getUsername());
        model.addAttribute("articleList", articleList);
        model.addAttribute("frag", "profile");
        model.addAttribute("user", user);
        model.addAttribute("TT", T);
        System.out.println(articleList);
        return "fragments/profile :: article-main-content";
    }

    @GetMapping("/myblogs")
    public String blogs(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users currentUser = (Users) authentication.getPrincipal();
        int userid = (int) currentUser.getId(); // 🎉 直接获取 userId

        boolean T=false;
        List<Role> roles = usermapper.getUserRolesByUid(userid);
        for (Role role : roles) {
            if (role.getName().equals("ROLE_ADMIN")) {
                T = true;
                break;
            }
        }
        List<Article> articleList = articleService.selectAll(0);
        for (Article aRticle : articleList) {
            String html = MarkdownUtils.markdownToHtml(aRticle.getContent());
            aRticle.setContent(html); // 替换内容
            articleService.updatalike(aRticle.getId());
        }

        Users user = usermapper.findbyusername(currentUser.getUsername());
        model.addAttribute("articleList", articleList);
        model.addAttribute("frag", "profile");
        model.addAttribute("user", user);
        model.addAttribute("TT", T);
        return "fragments/profile :: article-main-content";
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
            articleService.updatalike(aRticle.getId());

        }

        Users user = usermapper.findbyusername(currentUser.getUsername());
        model.addAttribute("user", user);
        model.addAttribute("articleList", articleList);
        model.addAttribute("TT", T);
        return "fragments/profile :: content"; // 只返回中间部分
    }

    @GetMapping("/gate/fragment")
    public String gateFragment(Model model) {
        List<Article> articleList = articleService.selectAll(1);
        for (Article aRticle : articleList) {
            String html = MarkdownUtils.markdownToHtml(aRticle.getContent());
            aRticle.setContent(html);
            articleService.updatalike(aRticle.getId());
        }
        model.addAttribute("articleList", articleList);
        model.addAttribute("frag", "feed");
        return "fragments/feed :: content"; // 只返回中间部分
    }

    @GetMapping("/favorite/fragment")
    public String favoriteFragment(Model model, @AuthenticationPrincipal Users user) {
        List<folder> userFolders = favoriteservice.getfolder(user.getId());
        model.addAttribute("folders", userFolders);
        model.addAttribute("fragg", "feed");
        return "fragments/favourite :: content";
    }

    @GetMapping("/allmessages/fragment")
    public String messageFragment(Model model, @AuthenticationPrincipal Users user) {
        List<Users> usersList = messageservice.getallUser();
        model.addAttribute("userList", usersList);
        model.addAttribute("fragg", "Allmessage-message");
        model.addAttribute("frag", "messages");
        model.addAttribute("usertemp",null);
        return "fragments/Allmessages :: content";
    }

    @GetMapping("/article/{id}")
    public String showArticleDetail(@PathVariable int id, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users currentUser = (Users) authentication.getPrincipal();
        Article article = articleService.findarticle(id); // 获取文章及评论
        String html = MarkdownUtils.markdownToHtml(article.getContent());
        article.setContent(html);
        articleService.setview(id);
        List<Comment> comments = commentService.getCommentsForArticle(id);
        boolean isfollow = followService.isFollower(currentUser.getId(), article.getAuthor_id());
        List<folder> userFolders = favoriteservice.getfolder(currentUser.getId());
        model.addAttribute("favoriteFolders", userFolders);
        model.addAttribute("isFollowed", isfollow);
        model.addAttribute("comments", comments);
        model.addAttribute("user", currentUser);
        model.addAttribute("article", article);
        model.addAttribute("frag", "messages");
        return "article-detail"; // 对应 templates/article-detail.html
    }

    @PostMapping("/like/article")
    @ResponseBody
    public Map<String, Object> likeArticle(@RequestParam("article_id") int article_id) {
        System.out.println(article_id);
        articleService.updataSupport(article_id);
        Article article = articleService.findarticle(article_id);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("like_count", article.getLike_count());
        return response;
    }

    @PostMapping("/favorite-folder/create")
    @ResponseBody
    public Map<String, Object> createFolder(@RequestBody Map<String, String> request,
                                            @AuthenticationPrincipal Users user) {
        String folderName = request.get("name");
        if (folderName == null || folderName.isBlank()) {
            return Map.of("success", false, "message", "名称不能为空");
        }

        favoriteservice.insertfolder(folderName,user.getId());
        return Map.of("success", true);
    }

    @GetMapping("/favorite/folder/{folderId}")
    public String loadFolderContent(@PathVariable Integer folderId, Model model,@AuthenticationPrincipal Users user) {
        List<Article> articles = favoriteservice.getarticle(user.getId(),folderId);
        model.addAttribute("articleList",articles);
        model.addAttribute("fragg", "feed");
        return "fragments/feed :: content"; // 返回局部视图片段
    }

    //文章搜索
    @GetMapping("/search/content")
    public String searchByContent(@RequestParam("keyword") String keyword, Model model) {
        List<Article> articles = articleService.search(keyword);
        for (Article aRticle : articles) {
            String html = MarkdownUtils.markdownToHtml(aRticle.getContent());
            aRticle.setContent(html); // 替换内容
        }
        model.addAttribute("articleList", articles);
        model.addAttribute("keyword", keyword);
        model.addAttribute("frag", "feed");
        return "home";
    }

    //审核
    @PostMapping("/accept/{articleId}")
    @ResponseBody
    public String acceptArticle(@PathVariable int articleId) {
        articleService.yes(articleId); // 假设“approved”是通过状态
        return "success";
    }

    @PostMapping("/refuse/{articleId}")
    @ResponseBody
    public String refuseArticle(@PathVariable int articleId) {
        articleService.no(articleId); // 假设“approved”是通过状态
        return "success";
    }

}
