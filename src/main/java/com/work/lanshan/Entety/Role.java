package com.work.lanshan.Entety;

import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

@Data
public class Role implements Serializable {
    @Getter
    private int id;
    private String name;

}
