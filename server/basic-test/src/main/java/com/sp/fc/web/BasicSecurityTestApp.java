package com.sp.fc.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@SpringBootApplication
public class BasicSecurityTestApp {

    public static void main(String[] args) {
        SpringApplication.run(BasicSecurityTestApp.class, args);
    }



}
