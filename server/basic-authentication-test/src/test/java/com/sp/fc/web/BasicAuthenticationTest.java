package com.sp.fc.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BasicAuthenticationTest {

    @LocalServerPort
    int port;

    private RestTemplate restTemplate = new RestTemplate();


    @DisplayName("1. 인증 오류")
    @Test
    void test_1() {
        String url = format("http://localhost:%d%s", port, "/greeting");
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> {
            restTemplate.getForEntity(url, String.class);
        });
        assertEquals(401, exception.getRawStatusCode());
    }


    @DisplayName("2. 인증 성공")
    @Test
    void test_2(){

        String url = format("http://localhost:%d%s", port, "/greeting");
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "basic "+ Base64.getEncoder().encodeToString("user1:1111".getBytes()));
        HttpEntity entity = new HttpEntity("", headers);

        ResponseEntity<String> response = restTemplate.exchange(url,
                HttpMethod.GET, entity, String.class);

        assertEquals("Hello jongwon", response.getBody());
    }


}
