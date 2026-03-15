package com.work.lanshan.Entety;

import lombok.Data;

/**
 * 消息实体类
 * 对应数据库中的消息表，用于用户间私信
 */
@Data
public class Messages {
    private int id;
    private String content;
    private int sender_id;
    private int receiver_id;
    private boolean is_read;
    private String sender_avatar ;
}
