package com.sp.fc.user;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class PaperUserTestApp {

    public static void main(String[] args) {
        SpringApplication.run(PaperUserTestApp.class, args);
    }

    @Configuration
    @ComponentScan("com.sp.fc.user")
    @EnableJpaRepositories(basePackages = {
            "com.sp.fc.user.repository"
    })
    @EntityScan(basePackages = {
            "com.sp.fc.user.domain"
    })
    class Config {

    }

}
