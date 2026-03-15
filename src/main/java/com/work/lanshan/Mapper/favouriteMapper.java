package com.work.lanshan.Mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface favouriteMapper {
    /** 添加收藏 */
    void insertFavourite(int user_id, int folder_id, int article_id);
    /** 取消收藏 */
    void deleteFavourite(int user_id, int folder_id, int article_id);
}
