package com.work.lanshan.config;

import com.work.lanshan.Entety.Article;
import com.work.lanshan.Mapper.ArticleMapper;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleService {
    private final ArticleMapper articleMapper;

    public ArticleService(ArticleMapper articleMapper) {
        this.articleMapper = articleMapper;
    }

    public List<Article>  getArticle(int id,int status) {

        return articleMapper.selectByAuthorIdandstatus(id, status);
    }

    public Article findarticle(int id) {
        return articleMapper.selectById(id);
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

    public int deleteArticle(int id) {
        return articleMapper.delete(id);
    }

    public List<Article> selectAll(int status) {
        return articleMapper.selectAll(status);
    }

    public int updataSupport(int article_id){
        return articleMapper.updatasupport(article_id);
    }

    public int updatalike(int article_id){
        return articleMapper.updatacommentcount(article_id);
    }

    public void setview(int article_id) {
        articleMapper.setview(article_id);
    }

    public List<Article> search(@Param("keyword") String keyword) {
        return articleMapper.search(keyword);
    }

    public void yes(int article_id) {
        articleMapper.setstatus(article_id);
    }

    public void no(int article_id) {
        articleMapper.setsatusnot(article_id);
    }


}


