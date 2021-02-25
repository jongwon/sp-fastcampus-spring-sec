package com.sp.fc.web.config;

import com.sp.fc.web.service.Paper;
import lombok.Getter;
import lombok.Setter;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.expression.WebSecurityExpressionRoot;

@Getter
@Setter
public class CustomMethodSecurityExpressionRoot extends SecurityExpressionRoot
    implements MethodSecurityExpressionOperations {

    MethodInvocation invocation;

    public CustomMethodSecurityExpressionRoot(Authentication authentication, MethodInvocation invocation) {
        super(authentication);
        this.invocation = invocation;
    }

    private Object filterObject;
    private Object returnObject;

    public boolean isStudent(){
        return getAuthentication().getAuthorities().stream()
                .filter(a->a.getAuthority().equals("ROLE_STUDENT"))
                .findAny().isPresent();
    }

    public boolean isTutor(){
        return getAuthentication().getAuthorities().stream()
                .filter(a->a.getAuthority().equals("ROLE_TUTOR"))
                .findAny().isPresent();
    }

    public boolean notPrepareSate(Paper paper){
        return paper.getState() != com.sp.fc.web.service.Paper.State.PREPARE;
    }

    @Override
    public Object getThis() {
        return this;
    }
}
