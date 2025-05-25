package com.work.lanshan.Entety;

import lombok.Data;
import lombok.Getter;
import org.springframework.context.support.BeanDefinitionDsl;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Data
public class Users implements UserDetails {
    private int id;
    private String username;
    private String password;
    private String email;
    private boolean enabled;
    private List<Role> roles;
    private String img;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for(Role r : roles) {
            authorities.add(new SimpleGrantedAuthority(r.getName()));
        }
        return authorities;
    }

}
