package com.work.lanshan.Mapper;

import com.work.lanshan.Entety.Messages;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MessagesMapper {
    void sendMessage(int sender_id, int receiver_id, String content);
    List<Messages> getMessage(int sender_id, int receiver_id);
    void setread(int receiver_id,int sender_id);
}
