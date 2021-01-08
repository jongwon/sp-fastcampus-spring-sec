package com.sp.fc.web.teacher;

import com.sp.fc.web.student.StudentAuthenticationToken;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TeacherManager implements AuthenticationProvider, InitializingBean {

    private Map<String, Teacher> teacherDB = new HashMap<>();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        TeacherAuthenticationToken token = (TeacherAuthenticationToken) authentication;
        if(teacherDB.containsKey(authentication.getCredentials())){
            Teacher teacher = teacherDB.get(authentication.getCredentials());
            token.setPrincipal(teacher);
            token.setAuthenticated(true);
        }else{
            token.setAuthenticated(false);
        }
        return token;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication == TeacherAuthenticationToken.class;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        List.of(
                new Teacher("choi", "최선생", 4)
        ).forEach(s-> teacherDB.put(s.getId(), s));
    }
}
