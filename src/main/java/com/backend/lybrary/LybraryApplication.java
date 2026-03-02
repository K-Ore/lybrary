package com.backend.lybrary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LybraryApplication {

    public static void main(String[] args) {
        SpringApplication.run(LybraryApplication.class, args);
    }

}