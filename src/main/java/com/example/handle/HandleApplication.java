package com.example.handle;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.example.handle.mapper")
@EnableScheduling // 启用定时任务功能
public class HandleApplication {

    public static void main(String[] args) {
        SpringApplication.run(HandleApplication.class, args);
    }

}
