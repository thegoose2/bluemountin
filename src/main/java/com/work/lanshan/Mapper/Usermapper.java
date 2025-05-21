package com.work.lanshan.Mapper;

import com.work.lanshan.Entety.Role;
import com.work.lanshan.Entety.Users;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface Usermapper {
    void insertuser(Users user);
    Users findbyusername(String username);
    void setuserrole(int userid,int roleid);
    List<Role> getUserRolesByUid(int id);
}
