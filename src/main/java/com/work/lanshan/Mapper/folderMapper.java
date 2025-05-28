package com.work.lanshan.Mapper;

import com.work.lanshan.Entety.Article;
import com.work.lanshan.Entety.Users;
import com.work.lanshan.Entety.folder;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface folderMapper {
    void deleteFolder(int id);
    void insertfolder(int user_id, String name);
    List<folder> getfolder(int user_id);
    List<Article> getarticle(int user_id,int folder_id);
}
