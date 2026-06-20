package com.fatigue.risk;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.fatigue.risk.mapper")
public class FatigueRiskApplication {
    public static void main(String[] args) {
        SpringApplication.run(FatigueRiskApplication.class, args);
    }
}
