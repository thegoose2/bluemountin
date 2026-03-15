package com.work.lanshan.Mapper;

import com.work.lanshan.Entety.Article;
import io.lettuce.core.dynamic.annotation.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ArticleMapper {
    /** 插入文章 */
    int insert(Article article);
    /** 更新文章 */
    int update(Article article);
    /** 根据作者ID和状态查询文章列表 */
    List<Article> selectByAuthorIdandstatus(int id,int status);
    /** 分页查询已发布的文章 */
    List<Article> selectPublished(@Param("offset") int offset, @Param("limit") int limit);
    /** 软删除文章（放入回收站） */
    int softDelete(Long id);
    /** 永久删除文章 */
    int delete(int id);
    /** 根据ID查询文章 */
    Article selectById(int id);
    /** 查询所有指定状态的文章 */
    List<Article> selectAll(int status);
    /** 增加文章点赞数 */
    int updatasupport(int article_id);
    /** 增加文章评论数 */
    int updatacommentcount(int article_id);
    /** 增加文章浏览量 */
    void setview(int article_id);
    /** 根据关键字搜索文章 */
    List<Article> search(@Param("keyword") String keyword);
    /** 审核通过文章 */
    void setstatus(int article_id);
    /** 审核拒绝文章 */
    void setsatusnot(int article_id);
    /** 查询关注用户发布的文章 */
    List<Article> selectFollow(int userId);
}
