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

    public void insertfolder(String name, int user_id) {
        foldermapper.insertfolder(user_id, name);
    }

    public void deletfolder(int id) {
        foldermapper.deleteFolder(id);
    }

    public void insertfavorite (int user_id, int folder_id,int article_id) {
        favouritemapper.insertFavourite(user_id, folder_id, article_id);
    }

    public void deletfavorite (int user_id, int folder_id,int article_id) {
        favouritemapper.deleteFavourite(user_id, folder_id, article_id);
    }

    public List<folder> getfolder(int user_id) {
        return foldermapper.getfolder(user_id);
    }

    public List<Article> getarticle(int user_id,int folder_id) {
        return foldermapper.getarticle(user_id,folder_id);
    }
}
