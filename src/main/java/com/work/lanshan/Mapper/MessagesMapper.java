package com.work.lanshan.Mapper;

import com.work.lanshan.Entety.Messages;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MessagesMapper {
    /** 发送私信 */
    void sendMessage(int sender_id, int receiver_id, String content);
    /** 获取两个用户之间的私信记录 */
    List<Messages> getMessage(int sender_id, int receiver_id);
    /** 将消息标记为已读 */
    void setread(int receiver_id,int sender_id);
}
