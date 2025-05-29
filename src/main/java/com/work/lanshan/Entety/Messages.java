package com.work.lanshan.Entety;

import lombok.Data;

@Data
public class Messages {
    private int id;
    private String content;
    private int sender_id;
    private int receiver_id;
    private boolean is_read;
    private String sender_avatar ;
}
