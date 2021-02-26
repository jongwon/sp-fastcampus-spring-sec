package com.sp.fc.web.config;

import com.sp.fc.web.controller.PaperController;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.log.LogMessage;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.annotation.SecuredAnnotationSecurityMetadataSource;
import org.springframework.security.access.method.MethodSecurityMetadataSource;
import org.springframework.security.access.prepost.PrePostAnnotationSecurityMetadataSource;
import org.springframework.util.ClassUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

public class CustomMetadataSource implements MethodSecurityMetadataSource {

    PrePostAnnotationSecurityMetadataSource prePostAnnotationSecurityMetadataSource;

    SecuredAnnotationSecurityMetadataSource securedAnnotationSecurityMetadataSource;

    @Override
    public Collection<ConfigAttribute> getAttributes(Method method, Class<?> targetClass) {
        CustomSecurityTag annotation = findAnnotation(method, targetClass, CustomSecurityTag.class);
        if(annotation != null){
            return List.of(new SecurityConfig(annotation.value()));
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
        return MethodInvocation.class.isAssignableFrom(clazz);
    }

    private <A extends Annotation> A findAnnotation(Method method, Class<?> targetClass, Class<A> annotationClass) {
        Method specificMethod = ClassUtils.getMostSpecificMethod(method, targetClass);
        A annotation = AnnotationUtils.findAnnotation(specificMethod, annotationClass);
        if (annotation != null) {
            return annotation;
        }
        return annotation;
    }
}
