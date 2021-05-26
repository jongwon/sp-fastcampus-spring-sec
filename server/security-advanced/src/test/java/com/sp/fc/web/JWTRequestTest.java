package com.sp.fc.web;

import com.sp.fc.user.domain.SpUser;
import com.sp.fc.user.repository.SpUserRepository;
import com.sp.fc.user.service.SpUserService;
import com.sp.fc.web.config.UserLoginForm;
import com.sp.fc.web.test.WebIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JWTRequestTest extends WebIntegrationTest {


    @Autowired
    private SpUserRepository userRepository;

    @Autowired
    private SpUserService userService;

    @BeforeEach
    void before(){
        userRepository.deleteAll();

        SpUser user = userService.save(SpUser.builder()
                .email("user1")
                .password("1111")
                .enabled(true)
                .build());
        userService.addAuthority(user.getUserId(), "ROLE_USER");
    }

    @DisplayName("1. hello 메시지를 받아온다... ")
    @Test
    void test_1(){

        RestTemplate client = new RestTemplate();

        HttpEntity<UserLoginForm> body = new HttpEntity<>(
                UserLoginForm.builder().username("user1").password("1111").build()
        );

        ResponseEntity<SpUser> resp1 = client.exchange(uri("/login"), HttpMethod.POST, body, SpUser.class);
        System.out.println(resp1.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0));
        System.out.println(resp1.getBody());

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.AUTHORIZATION, resp1.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0));
        body = new HttpEntity<>(null, header);
        ResponseEntity<String> resp2 = client.exchange(uri("/greeting"), HttpMethod.GET, body, String.class);

        assertEquals("hello", resp2.getBody());

    }

}
