package com.work.lanshan.Mapper;

import com.work.lanshan.Entety.Role;
import com.work.lanshan.Entety.Users;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface Usermapper {
    /** 插入新用户 */
    void insertuser(Users user);
    /** 根据用户名查询用户 */
    Users findbyusername(String username);
    /** 设置用户角色 */
    void setuserrole(int userid,int roleid);
    /** 根据用户ID查询角色列表 */
    List<Role> getUserRolesByUid(int id);
    /** 根据ID查询用户 */
    Users findbyid(int id);
    /** 更新用户头像 */
    void updateAvatar(long userId,String avatarUrl);
    /** 获取所有用户列表 */
    List<Users> getallusers();
}
