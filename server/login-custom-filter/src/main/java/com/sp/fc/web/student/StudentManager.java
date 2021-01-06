package com.sp.fc.web.student;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class StudentManager implements AuthenticationProvider, InitializingBean {

    private Map<String, Student> studentDB = new HashMap<>();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        StudentAuthenticationToken token = (StudentAuthenticationToken) authentication;
        if(studentDB.containsKey(authentication.getCredentials())){
            Student student = studentDB.get(authentication.getCredentials());
            token.setPrincipal(student);
            token.setAuthenticated(true);
        }else{
            token.setAuthenticated(false);
        }
        return token;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication == StudentAuthenticationToken.class;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        List.of(
                new Student("hong@student.com", "홍길동", 3),
                new Student("kang@student.com", "강아지", 4),
                new Student("yoon@student.com", "윤봉길", 5)
        ).forEach(s->studentDB.put(s.getId(), s));
    }
}
