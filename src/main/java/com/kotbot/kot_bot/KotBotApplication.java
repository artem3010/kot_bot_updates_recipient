package com.kotbot.kot_bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class KotBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(KotBotApplication.class, args);
    }

}
