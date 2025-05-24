package com.work.lanshan.Mapper;

import com.work.lanshan.Entety.Article;
import io.lettuce.core.dynamic.annotation.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ArticleMapper {
    int insert(Article article);
    int update(Article article);
    Article selectById(Long id);
    List<Article> selectPublished(@Param("offset") int offset, @Param("limit") int limit);
    int softDelete(Long id);
    int delete(Long id);
}
