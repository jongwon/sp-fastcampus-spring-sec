package com.sp.fc.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "com.sp.fc.web.config",
        "com.sp.fc.web.paper"
})
public class AuthorityACLApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthorityACLApplication.class, args);
    }

}
