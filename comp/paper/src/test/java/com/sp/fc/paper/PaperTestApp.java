package com.sp.fc.paper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {
        "com.sp.fc.config"
})
public class PaperTestApp {

    public static void main(String[] args) {
        SpringApplication.run(PaperTestApp.class, args);
    }

    @Configuration
    @EntityScan(basePackages = {
            "com.sp.fc.user.domain",
            "com.sp.fc.paper.domain"
    })
    @EnableJpaRepositories(basePackages = {
            "com.sp.fc.user.repository",
            "com.sp.fc.paper.repository"
    })
    class JpaConfig {

    }
}
