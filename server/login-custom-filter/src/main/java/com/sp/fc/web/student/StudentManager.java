package com.sp.fc.web.student;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class StudentManager implements AuthenticationProvider, InitializingBean {

    private Map<String, Student> studentDB = new HashMap<>();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
        if(studentDB.containsKey(authentication.getName())){
            Student student = studentDB.get(authentication.getName());
            StudentAuthenticationToken authToken = StudentAuthenticationToken.builder()
                    .principal(student)
                    .details(""+student.getGrade())
                    .authorities(student.getAuthorities())
                    .authenticated(true)
                    .build();
            return authToken;
        }else {
            return null;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication == UsernamePasswordAuthenticationToken.class;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        List.of(
                new Student("hong", "홍길동", 3,
                        Set.of(new SimpleGrantedAuthority("ROLE_STUDENT")),
                        "s",
                        true),
                new Student("kang", "강아지", 4,
                        Set.of(new SimpleGrantedAuthority("ROLE_STUDENT")),
                        "s",
                        true),
                new Student("ho", "호랑", 5,
                        Set.of(new SimpleGrantedAuthority("ROLE_STUDENT")),
                        "s",
                        true)
        ).forEach(s->studentDB.put(s.getId(), s));
    }
}
