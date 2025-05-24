package com.work.lanshan.controller;

import com.work.lanshan.Components.MarkdownUtils;
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

    @GetMapping("/{id}")
    public List<Article> getArticle(@PathVariable Long id) {
        return articleService.getArticle(id);
    }

    @GetMapping
    public List<Article> listArticles(@RequestParam(defaultValue = "1") int page,
                                      @RequestParam(defaultValue = "10") int size) {
        return articleService.listPublished(page, size);
    }

    @PostMapping("/unsure")
    public String createArticle0(@RequestParam("content") String content, @RequestParam("title") String title,Article article,Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users currentUser = (Users) authentication.getPrincipal();
        Long userid = (long) currentUser.getId(); // 🎉 直接获取 userId
        article.setAuthorId(userid);
        article.setContent(content);
        article.setTitle(title);
        article.setStatus(0); // 默认草稿
        article.setViewCount(0);
        article.setLikeCount(0);
        article.setCommentCount(0);
        articleService.createArticle(article);
        List<Article> articleList = articleService.getArticle(userid);
        for (Article aRticle : articleList) {
            String html = MarkdownUtils.markdownToHtml(aRticle.getContent());
            aRticle.setContent(html); // 替换内容
        }
        model.addAttribute("articleList", articleList);
        model.addAttribute("frag", "profile");
        return "home"; // home.html 模板中包含 fragments/profile.html 片段
    }

    @PostMapping("/sure")
    public String createArticle1(@RequestParam("content") String content, @RequestParam("title") String title, Article article, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users currentUser = (Users) authentication.getPrincipal();
        Long userid = (long) currentUser.getId(); // 🎉 直接获取 userId
        article.setAuthorId(userid);
        article.setContent(content);
        article.setTitle(title);
        article.setStatus(1); // 发布待审和文章
        article.setViewCount(0);
        article.setLikeCount(0);
        article.setCommentCount(0);
        articleService.createArticle(article);
        List<Article> articleList = articleService.getArticle(userid);
        for (Article aRticle : articleList) {
            String html = MarkdownUtils.markdownToHtml(aRticle.getContent());
            aRticle.setContent(html); // 替换内容
        }
        model.addAttribute("articleList", articleList);
        model.addAttribute("frag", "profile");
        return "home"; // home.html 模板中包含 fragments/profile.html 片段
    }


    @PutMapping("/{id}")
    public String updateArticle(@PathVariable Long id, @RequestBody Article article) {
        article.setId(id);
        articleService.updateArticle(article);
        return "文章更新成功";
    }

    @DeleteMapping("/{id}")
    public String deleteArticle(@PathVariable Long id) {
        articleService.softDeleteArticle(id);
        return "文章已移入回收站";
    }

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

}
