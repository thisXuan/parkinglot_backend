package com.parkinglot_backend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // 启用计划任务
@MapperScan("com.parkinglot_backend.mapper")
public class ParkinglotBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ParkinglotBackendApplication.class, args);
    }

}
