package com.work.lanshan.Entety;

import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

/**
 * 角色实体类
 * 对应数据库中的角色表，用于权限管理
 */
@Data
public class Role implements Serializable {
    @Getter
    private int id;
    private String name;

}
