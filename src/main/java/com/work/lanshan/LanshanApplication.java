package com.work.lanshan;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.io.InputStream;

@SpringBootApplication
public class LanshanApplication {

    /**
     * Spring Boot应用程序入口类
     */
    public LanshanApplication() throws IOException {
    }

    /**
     * 启动Spring Boot应用
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(LanshanApplication.class, args);
    }

}
