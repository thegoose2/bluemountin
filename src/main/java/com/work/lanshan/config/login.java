package com.work.lanshan.config;

import com.work.lanshan.Components.MyPersistentTokenRepository;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.annotation.web.configurers.RememberMeConfigurer;
import org.springframework.ui.Model;
import com.work.lanshan.Entety.Users;
import com.work.lanshan.Mapper.Usermapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Service;

@Configuration
@EnableWebSecurity
public class login {

    @Autowired
    private MyPersistentTokenRepository tokenRepository;

    @Bean
    public SecurityFilterChain FilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // 公共页面允许所有人访问（登录页、静态资源等）
                        .requestMatchers("/login/**", "/css/**", "/js/**", "/img/**","/", "/home/**","/registerer/**","/edit/**","/editor/**","/static/**").permitAll()

                        // 用户页面，如发帖、资料页，需要 USER 或 ADMIN 权限
                        .requestMatchers("/profile","/post/**","/articles/**").hasAnyRole("USER", "ADMIN")

                        // 管理员页面需要 ADMIN 权限
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // 其他请求登录即可
                        .anyRequest().authenticated()
                )
                .formLogin(form->form
                        .loginPage("/login")
                        .loginProcessingUrl("/dologin")
                        .defaultSuccessUrl("/profile", true)
                        .failureUrl("/loginerror")
                        .permitAll()
                )
                .rememberMe(rememberMe -> rememberMe
                        .tokenRepository(tokenRepository)  // 使用你的 Redis 实现
                        .userDetailsService(new UserServiceImpl())        // 提供用户验证
                        .tokenValiditySeconds(14 * 24 * 60 * 60)         // 设置有效期（秒）
                )
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers
                .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)); // ✅ 允许 iframe 同源访问// ✅ 正确的新方式
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //登录
    @Service
    public static class UserServiceImpl implements UserDetailsService {
        @Autowired
        private Usermapper usermapper;
        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            Users user = usermapper.findbyusername(username);
            if (user == null) {
                throw new UsernameNotFoundException("用户不存在！");
            }
            else{
                if(!user.getPassword().equals(usermapper.findbyusername(username).getPassword())){
                    throw new UsernameNotFoundException("密码错误！");
                }
                else{
                    user.setRoles(usermapper.getUserRolesByUid(user.getId()));
                    // 假设你在登录成功后执行以下代码：
                    return user;
                }
            }
        }
    }



}

