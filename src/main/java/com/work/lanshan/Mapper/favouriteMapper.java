package com.work.lanshan.Mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface favouriteMapper {
    void insertFavourite(int user_id, int folder_id, int article_id);
    void deleteFavourite(int user_id, int folder_id, int article_id);
}
