package com.sp.fc.web.student;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentAuthenticationToken implements Authentication {

    private String credentials; // id
    private final Set<GrantedAuthority> authorities = Set.of(new SimpleGrantedAuthority("ROLE_STUDENT"));
    private Student principal;
    private boolean authenticated;

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public String getName() {
        return principal == null ? "" : principal.getName();
    }
}
