package com.sp.fc.web.teacher;

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
public class TeacherManager implements AuthenticationProvider, InitializingBean {

    private Map<String, Teacher> teacherDB = new HashMap<>();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
        if(teacherDB.containsKey(authentication.getName())){
            Teacher teacher = teacherDB.get(authentication.getName());
            TeacherAuthenticationToken authToken = TeacherAuthenticationToken.builder()
                    .principal(teacher)
                    .details(""+teacher.getGrade())
                    .authorities(teacher.getAuthorities())
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
                new Teacher("choi", "최선생", 4,
                        Set.of(new SimpleGrantedAuthority("ROLE_TEACHER")),
                        "t",
                        true)
        ).forEach(t-> teacherDB.put(t.getId(), t));
    }
}
