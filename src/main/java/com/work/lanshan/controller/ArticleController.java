package com.work.lanshan.controller;

import com.work.lanshan.Entety.Article;
import com.work.lanshan.config.ArticleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/articles")
public class ArticleController {
    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/{id}")
    public Article getArticle(@PathVariable Long id) {
        return articleService.getArticle(id);
    }

    @GetMapping
    public List<Article> listArticles(@RequestParam(defaultValue = "1") int page,
                                      @RequestParam(defaultValue = "10") int size) {
        return articleService.listPublished(page, size);
    }

    @PostMapping
    public String createArticle(@RequestBody Article article) {
        article.setStatus(0); // 默认草稿
        articleService.createArticle(article);
        return "文章创建成功";
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
}
