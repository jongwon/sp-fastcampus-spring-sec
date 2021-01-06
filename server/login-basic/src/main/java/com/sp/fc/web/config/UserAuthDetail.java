package com.sp.fc.web.config;

import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Component
public class UserAuthDetail implements AuthenticationDetailsSource<HttpServletRequest, RequestInfo> {

    @Override
    public RequestInfo buildDetails(HttpServletRequest context) {
        return RequestInfo.builder()
                .loginTime(LocalDateTime.now())
                .remoteIp(context.getRemoteAddr())
                .build();
    }

}
