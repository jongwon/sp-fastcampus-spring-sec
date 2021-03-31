package com.sp.fc.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan("com.sp.fc.user")
@EnableJpaRepositories(basePackages = {
        "com.sp.fc.user.repository"
})
@EntityScan(basePackages = {
        "com.sp.fc.user.domain"
})
public class PaperUserModule {

}
