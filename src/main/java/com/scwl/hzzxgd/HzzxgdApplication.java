package com.scwl.hzzxgd;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@MapperScan("com.scwl.hzzxgd.mapper")
@EnableScheduling
public class HzzxgdApplication {

    public static void main(String[] args) {
        SpringApplication.run(HzzxgdApplication.class, args);
    }

}
