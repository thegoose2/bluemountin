package com.work.lanshan.config;

import com.work.lanshan.Entety.Article;
import com.work.lanshan.Entety.favourite;
import com.work.lanshan.Entety.folder;
import com.work.lanshan.Mapper.favouriteMapper;
import com.work.lanshan.Mapper.folderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class favoriteService {
    @Autowired
    folderMapper foldermapper;

    @Autowired
    favouriteMapper favouritemapper;

    /**
     * 创建收藏文件夹
     * @param name 文件夹名称
     * @param user_id 用户ID
     */
    public void insertfolder(String name, int user_id) {
        foldermapper.insertfolder(user_id, name);
    }

    /**
     * 删除收藏文件夹
     * @param id 文件夹ID
     */
    public void deletfolder(int id) {
        foldermapper.deleteFolder(id);
    }

    /**
     * 添加文章到收藏夹
     * @param user_id 用户ID
     * @param folder_id 文件夹ID
     * @param article_id 文章ID
     */
    public void insertfavorite (int user_id, int folder_id,int article_id) {
        favouritemapper.insertFavourite(user_id, folder_id, article_id);
    }

    /**
     * 从收藏夹移除文章
     * @param user_id 用户ID
     * @param folder_id 文件夹ID
     * @param article_id 文章ID
     */
    public void deletfavorite (int user_id, int folder_id,int article_id) {
        favouritemapper.deleteFavourite(user_id, folder_id, article_id);
    }

    /**
     * 获取用户的所有收藏文件夹
     * @param user_id 用户ID
     * @return 文件夹列表
     */
    public List<folder> getfolder(int user_id) {
        return foldermapper.getfolder(user_id);
    }

    /**
     * 获取指定文件夹中的文章列表
     * @param user_id 用户ID
     * @param folder_id 文件夹ID
     * @return 文章列表
     */
    public List<Article> getarticle(int user_id,int folder_id) {
        return foldermapper.getarticle(user_id,folder_id);
    }
}
