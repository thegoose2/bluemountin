package com.work.lanshan.Entety;

import lombok.Data;

import java.util.List;

@Data
public class folder {
    private int id;
    private String name;
    private int user_id;
    private List<Article> totalArticles;
}
