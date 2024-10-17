package com.parkinglot_backend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.parkinglot_backend.mapper")
public class ParkinglotBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ParkinglotBackendApplication.class, args);
    }

}
