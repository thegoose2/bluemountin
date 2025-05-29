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

    public void sendMessage(int send_id,int receiver_id,String content){
        messagesMapper.sendMessage(send_id,receiver_id,content);
    }

    public List<Messages> getMessage(int sender_id, int receiver_id){
        return messagesMapper.getMessage(sender_id,receiver_id);
    }

    public List<Users> getallUser(){
        return usermapper.getallusers();
    }
}
