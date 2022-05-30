package com.myc.hrmanager;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.myc.hrmanager.mapper")

public class HrmanagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(HrmanagerApplication.class, args);
    }

}
