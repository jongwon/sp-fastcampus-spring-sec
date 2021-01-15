package com.sp.fc.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {
        "com.sp.fc.user",
        "com.sp.fc.web"
})
@EntityScan(basePackages = {
        "com.sp.fc.user.domain"
})
@EnableJpaRepositories(basePackages = {
        "com.sp.fc.user.repository"
})
public class UserDetailsTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserDetailsTestApplication.class, args);
    }

}
