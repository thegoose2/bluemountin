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


    /**
     * 首页，显示动态feed内容
     * @param model 模型
     * @return 首页视图
     */
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

    /**
     * 用户个人主页，显示profile内容
     * @param model 模型
     * @return 个人主页视图
     */
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

    /**
     * 登录页
     * @param model 模型
     * @return 登录视图
     */
    // 登录页
    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("frag", "login-login");
        return "login";
    }

    /**
     * 登录错误页
     * @param model 模型
     * @return 登录视图（带错误信息）
     */
    @GetMapping("/loginerror")
    public String loginerror(Model model) {
        model.addAttribute("frags", "login-login");
        model.addAttribute("error", "用户或密码错误！");
        return "login";
    }

    /**
     * 注册页
     * @param model 模型
     * @return 登录视图（显示注册页面）
     */
    @GetMapping("/registerer")
    public String registered(Model model) {
        model.addAttribute("frag", "login-registered");
        return "login";
    }

    /**
     * 获取注册页面片段
     * @return 注册片段
     */
    @GetMapping("/registerer/fragment")
    public String registereded() {
        return "fragments/login-registered :: login-content";
    }

    /**
     * 获取登录页面片段
     * @return 登录片段
     */
    @GetMapping("/login/fragment")
    public String logined() {
        return "fragments/login-login :: login-content";
    }

    //后台登录与注册
    @Autowired
    private com.work.lanshan.Mapper.Usermapper usermapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * 用户注册
     * @param user 用户对象
     * @param model 模型
     * @param confirmpassword 确认密码
     * @return 登录视图
     */
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

    /**
     * 文章编辑页
     * @return 编辑视图
     */
    @GetMapping("/edit")
    public String edit() {
        return "edit";
    }

    /**
     * 上传用户头像
     * @param file 头像文件
     * @param model 模型
     * @return 个人主页
     * @throws IOException IO异常
     */
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

    /**
     * 我的博客页面（指定状态）
     * @param temp 文章状态
     * @param model 模型
     * @return 个人主页片段
     */
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

    /**
     * 我的草稿箱（所有草稿文章）
     * @param model 模型
     * @return 个人主页片段
     */
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

    /**
     * 获取个人主页片段
     * @param model 模型
     * @return 个人主页片段
     */
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

    /**
     * 获取首页动态feed片段
     * @param model 模型
     * @return feed片段
     */
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

    /**
     * 获取关注用户的动态feed片段
     * @param model 模型
     * @param user 当前用户
     * @return feed片段
     */
    @GetMapping("/follow/fragment")
    public String followFragment(Model model,@AuthenticationPrincipal Users user) {
        List<Article> articleList = articleService.selectFollow(user.getId());
        for (Article aRticle : articleList) {
            String html = MarkdownUtils.markdownToHtml(aRticle.getContent());
            aRticle.setContent(html);
            articleService.updatalike(aRticle.getId());
        }
        model.addAttribute("articleList", articleList);
        model.addAttribute("frag", "feed");
        return "fragments/feed :: content"; // 只返回中间部分
    }

    /**
     * 获取收藏夹片段
     * @param model 模型
     * @param user 当前用户
     * @return 收藏片段
     */
    @GetMapping("/favorite/fragment")
    public String favoriteFragment(Model model, @AuthenticationPrincipal Users user) {
        List<folder> userFolders = favoriteservice.getfolder(user.getId());
        model.addAttribute("folders", userFolders);
        model.addAttribute("fragg", "feed");
        return "fragments/favourite :: content";
    }

    /**
     * 获取消息页面片段（所有用户列表）
     * @param model 模型
     * @param user 当前用户
     * @return 消息片段
     */
    @GetMapping("/allmessages/fragment")
    public String messageFragment(Model model, @AuthenticationPrincipal Users user) {
        List<Users> usersList = messageservice.getallUser();
        model.addAttribute("userList", usersList);
        model.addAttribute("fragg", "Allmessage-message");
        model.addAttribute("frag", "messages");
        model.addAttribute("usertemp",null);
        return "fragments/Allmessages :: content";
    }

    /**
     * 文章详情页
     * @param id 文章ID
     * @param model 模型
     * @return 文章详情视图
     */
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

    /**
     * 点赞文章
     * @param article_id 文章ID
     * @return 点赞结果（包含新的点赞数）
     */
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

    /**
     * 创建收藏文件夹
     * @param request 请求参数（包含文件夹名称）
     * @param user 当前用户
     * @return 创建结果
     */
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

    /**
     * 加载收藏文件夹中的文章
     * @param folderId 文件夹ID
     * @param model 模型
     * @param user 当前用户
     * @return feed片段
     */
    @GetMapping("/favorite/folder/{folderId}")
    public String loadFolderContent(@PathVariable Integer folderId, Model model,@AuthenticationPrincipal Users user) {
        List<Article> articles = favoriteservice.getarticle(user.getId(),folderId);
        for (Article aRticle : articles) {
            String html = MarkdownUtils.markdownToHtml(aRticle.getContent());
            aRticle.setContent(html); // 替换内容
        }
        model.addAttribute("articleList",articles);
        model.addAttribute("fragg", "feed");
        return "fragments/feed :: content"; // 返回局部视图片段
    }

    /**
     * 根据关键字搜索文章
     * @param keyword 搜索关键字
     * @param model 模型
     * @return 首页视图（显示搜索结果）
     */
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

    /**
     * 审核通过文章
     * @param articleId 文章ID
     * @return 审核结果
     */
    //审核
    @PostMapping("/accept/{articleId}")
    @ResponseBody
    public String acceptArticle(@PathVariable int articleId) {
        articleService.yes(articleId); // 假设“approved”是通过状态
        return "success";
    }

    /**
     * 审核拒绝文章
     * @param articleId 文章ID
     * @return 审核结果
     */
    @PostMapping("/refuse/{articleId}")
    @ResponseBody
    public String refuseArticle(@PathVariable int articleId) {
        articleService.no(articleId); // 假设“approved”是通过状态
        return "success";
    }

}
