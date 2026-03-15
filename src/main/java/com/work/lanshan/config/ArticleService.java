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

    /**
     * 根据作者ID和状态获取文章列表
     * @param id 作者ID
     * @param status 文章状态
     * @return 文章列表
     */
    public List<Article>  getArticle(int id,int status) {

        return articleMapper.selectByAuthorIdandstatus(id, status);
    }

    /**
     * 根据ID查找文章
     * @param id 文章ID
     * @return 文章对象
     */
    public Article findarticle(int id) {
        return articleMapper.selectById(id);
    }

    /**
     * 分页获取已发布文章列表
     * @param page 页码
     * @param size 每页数量
     * @return 文章列表
     */
    public List<Article> listPublished(int page, int size) {
        int offset = (page - 1) * size;
        return articleMapper.selectPublished(offset, size);
    }

    /**
     * 创建新文章
     * @param article 文章对象
     * @return 插入结果
     */
    public int createArticle(Article article) {
        return articleMapper.insert(article);
    }

    /**
     * 更新文章
     * @param article 文章对象
     * @return 更新结果
     */
    public int updateArticle(Article article) {
        return articleMapper.update(article);
    }

    /**
     * 软删除文章（移入回收站）
     * @param id 文章ID
     * @return 删除结果
     */
    public int softDeleteArticle(Long id) {
        return articleMapper.softDelete(id);
    }

    /**
     * 永久删除文章
     * @param id 文章ID
     * @return 删除结果
     */
    public int deleteArticle(int id) {
        return articleMapper.delete(id);
    }

    /**
     * 获取所有指定状态的文章
     * @param status 文章状态
     * @return 文章列表
     */
    public List<Article> selectAll(int status) {
        return articleMapper.selectAll(status);
    }

    /**
     * 增加文章点赞数
     * @param article_id 文章ID
     * @return 更新结果
     */
    public int updataSupport(int article_id){
        return articleMapper.updatasupport(article_id);
    }

    /**
     * 增加文章评论数
     * @param article_id 文章ID
     * @return 更新结果
     */
    public int updatalike(int article_id){
        return articleMapper.updatacommentcount(article_id);
    }

    /**
     * 增加文章浏览量
     * @param article_id 文章ID
     */
    public void setview(int article_id) {
        articleMapper.setview(article_id);
    }

    /**
     * 搜索文章
     * @param keyword 关键字
     * @return 文章列表
     */
    public List<Article> search(@Param("keyword") String keyword) {
        return articleMapper.search(keyword);
    }

    /**
     * 审核通过文章
     * @param article_id 文章ID
     */
    public void yes(int article_id) {
        articleMapper.setstatus(article_id);
    }

    /**
     * 审核拒绝文章
     * @param article_id 文章ID
     */
    public void no(int article_id) {
        articleMapper.setsatusnot(article_id);
    }

    /**
     * 获取关注用户发布的文章
     * @param id 当前用户ID
     * @return 文章列表
     */
    public List<Article> selectFollow(int id) {
        return articleMapper.selectFollow(id);
    }


}


