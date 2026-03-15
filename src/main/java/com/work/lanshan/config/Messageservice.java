package com.work.lanshan.config;

import com.work.lanshan.Entety.Messages;
import com.work.lanshan.Entety.Users;
import com.work.lanshan.Mapper.MessagesMapper;
import com.work.lanshan.Mapper.Usermapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Messageservice {

    @Autowired
    MessagesMapper messagesMapper;

    @Autowired
    Usermapper usermapper;

    /**
     * 发送私信
     * @param send_id 发送者ID
     * @param receiver_id 接收者ID
     * @param content 消息内容
     */
    public void sendMessage(int send_id,int receiver_id,String content){
        messagesMapper.sendMessage(send_id,receiver_id,content);
    }

    /**
     * 获取两个用户之间的私信记录
     * @param sender_id 发送者ID
     * @param receiver_id 接收者ID
     * @return 消息列表
     */
    public List<Messages> getMessage(int sender_id, int receiver_id){
        return messagesMapper.getMessage(sender_id,receiver_id);
    }

    /**
     * 获取所有用户列表
     * @return 用户列表
     */
    public List<Users> getallUser(){
        return usermapper.getallusers();
    }
}
