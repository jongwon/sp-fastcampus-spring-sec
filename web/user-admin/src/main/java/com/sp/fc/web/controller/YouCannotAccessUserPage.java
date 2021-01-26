package com.sp.fc.web.controller;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;


public class YouCannotAccessUserPage extends AccessDeniedException {

    public YouCannotAccessUserPage() {
        super("유저페이지 접근 거부");
    }


}
