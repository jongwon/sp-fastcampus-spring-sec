package com.sp.fc.web.config;

import com.sp.fc.web.controller.PaperController;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.access.method.MethodSecurityMetadataSource;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

public class CustomMethodSecurityMetadataSource implements MethodSecurityMetadataSource {

    @Override
    public Collection<ConfigAttribute> getAttributes(Method method, Class<?> targetClass) {
        if(method.getName().equals("getPaper") && targetClass == PaperController.class){
            return List.of(new SecurityConfig("SCHOOL_PRIMARY"));
        }
        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return (MethodInvocation.class.isAssignableFrom(clazz));
    }
}
