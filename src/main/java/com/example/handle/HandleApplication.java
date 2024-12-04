package com.example.handle;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.handle.mapper")
public class HandleApplication {

    public static void main(String[] args) {
        SpringApplication.run(HandleApplication.class, args);
    }

}
