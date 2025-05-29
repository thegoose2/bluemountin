package com.work.lanshan.Mapper;

import com.work.lanshan.Entety.Article;
import io.lettuce.core.dynamic.annotation.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ArticleMapper {
    int insert(Article article);
    int update(Article article);
    List<Article> selectByAuthorIdandstatus(int id,int status);
    List<Article> selectPublished(@Param("offset") int offset, @Param("limit") int limit);
    int softDelete(Long id);
    int delete(int id);
    Article selectById(int id);
    List<Article> selectAll(int status);
    int updatasupport(int article_id);
    int updatacommentcount(int article_id);
    void setview(int article_id);
    List<Article> search(@Param("keyword") String keyword);
    void setstatus(int article_id);
    void setsatusnot(int article_id);
}
