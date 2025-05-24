package com.work.lanshan.config;

import com.work.lanshan.Entety.Article;
import com.work.lanshan.Mapper.ArticleMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleService {
    private final ArticleMapper articleMapper;

    public ArticleService(ArticleMapper articleMapper) {
        this.articleMapper = articleMapper;
    }

    public List<Article>  getArticle(Long id) {
        return articleMapper.selectByAuthorId(id);
    }

    public List<Article> listPublished(int page, int size) {
        int offset = (page - 1) * size;
        return articleMapper.selectPublished(offset, size);
    }

    public int createArticle(Article article) {
        return articleMapper.insert(article);
    }

    public int updateArticle(Article article) {
        return articleMapper.update(article);
    }

    public int softDeleteArticle(Long id) {
        return articleMapper.softDelete(id);
    }

    public int deleteArticle(Long id) {
        return articleMapper.delete(id);
    }


}


