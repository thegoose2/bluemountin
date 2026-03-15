package com.work.lanshan.Mapper;

import com.work.lanshan.Entety.Article;
import com.work.lanshan.Entety.Users;
import com.work.lanshan.Entety.folder;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface folderMapper {
    /** 删除文件夹 */
    void deleteFolder(int id);
    /** 创建文件夹 */
    void insertfolder(int user_id, String name);
    /** 获取用户的所有文件夹 */
    List<folder> getfolder(int user_id);
    /** 获取文件夹中的文章列表 */
    List<Article> getarticle(int user_id,int folder_id);
}
