package com.work.lanshan.controller;

import com.work.lanshan.Components.MarkdownUtils;
import com.work.lanshan.Entety.Comment;
import com.work.lanshan.Entety.Role;
import com.work.lanshan.Mapper.Usermapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.work.lanshan.Entety.Article;
import com.work.lanshan.Entety.Users;
import com.work.lanshan.config.ArticleService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;


@Controller
@RequestMapping("/articles")
public class ArticleController {
    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @Autowired
    private Usermapper usermapper;

    /**
     * 获取指定用户的文章列表
     * @param id 用户ID
     * @param status 文章状态
     * @return 文章列表
     */
    @GetMapping("/{id}")
    public List<Article> getArticle(@PathVariable int id,int status) {
        return articleService.getArticle(id, status);
    }

    /**
     * 分页获取已发布文章列表
     * @param page 页码
     * @param size 每页数量
     * @return 文章列表
     */
    @GetMapping
    public List<Article> listArticles(@RequestParam(defaultValue = "1") int page,
                                      @RequestParam(defaultValue = "10") int size) {
        return articleService.listPublished(page, size);
    }

    /**
     * 创建草稿文章
     * @param content 文章内容
     * @param article 文章对象
     * @param model 模型
     * @return 返回个人主页
     */
    @PostMapping("/unsure")
    public String createArticle0(@RequestParam("content") String content, Article article,Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users currentUser = (Users) authentication.getPrincipal();
        int userid = (int) currentUser.getId(); // 🎉 直接获取 userId
        article.setAuthor_id(userid);
        article.setContent(content);
        article.setStatus(0); // 默认草稿
        article.setView_count(0);
        article.setLike_count(0);
        article.setComment_count(0);
        articleService.createArticle(article);
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

        Users user = usermapper.findbyusername(currentUser.getUsername());
        model.addAttribute("user", user);
        model.addAttribute("articleList", articleList);
        model.addAttribute("frag", "profile");
        model.addAttribute("TT", T);
        return "home"; // home.html 模板中包含 fragments/profile.html 片段
    }

    /**
     * 创建待审核文章（发布文章）
     * @param content 文章内容
     * @param article 文章对象
     * @param model 模型
     * @return 返回个人主页
     */
    @PostMapping("/sure")
    public String createArticle1(@RequestParam("content") String content, Article article, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users currentUser = (Users) authentication.getPrincipal();
        int userid = (int) currentUser.getId(); // 🎉 直接获取 userId
        article.setAuthor_id(userid);
        article.setContent(content);
        article.setStatus(0); // 发布待审和文章
        article.setView_count(0);
        article.setLike_count(0);
        article.setComment_count(0);
        articleService.createArticle(article);
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

        Users user = usermapper.findbyusername(currentUser.getUsername());
        model.addAttribute("user", user);
        model.addAttribute("articleList", articleList);
        model.addAttribute("frag", "profile");
        model.addAttribute("TT", T);
        return "home"; // home.html 模板中包含 fragments/profile.html 片段
    }


    /**
     * 更新文章
     * @param id 文章ID
     * @param article 文章对象
     * @return 更新结果消息
     */
    @PutMapping("/{id}")
    public String updateArticle(@PathVariable int id, @RequestBody Article article) {
        article.setId(id);
        articleService.updateArticle(article);
        return "文章更新成功";
    }

    /**
     * 软删除文章（移入回收站）
     * @param id 文章ID
     * @return 删除结果消息
     */
    @DeleteMapping("/{id}")
    public String deleteArticle(@PathVariable Long id) {
        articleService.softDeleteArticle(id);
        return "文章已移入回收站";
    }

    /**
     * 上传文章图片
     * @param file 图片文件
     * @return 上传结果（包含图片URL）
     */
    @PostMapping("/upload/image")
    @ResponseBody
    public Map<String, Object> uploadImage(@RequestParam("editormd-image-file") MultipartFile file) throws IOException, IOException {
        String uploadDir = "E:/code/JAVA/pic/";
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

        String filename = UUID.randomUUID() + file.getOriginalFilename()
                .substring(file.getOriginalFilename().lastIndexOf("."));
        File dest = new File(uploadDir + filename);
        file.transferTo(dest);

        Map<String, Object> result = new HashMap<>();
        result.put("success", 1);
        result.put("message", "上传成功");
        result.put("url", "/uploads/" + filename);

        return result;
    }

    /**
     * 永久删除文章
     * @param id 文章ID
     * @return 删除结果
     */
    @PostMapping("/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteArticle(@PathVariable("id") int id) {
        articleService.deleteArticle(id); // 自行实现
        return ResponseEntity.ok("Deleted");
    }

}
