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
    private Set<GrantedAuthority> authorities;
    private Student principal;
    private boolean authenticated;
    private String details;

    @Override
    public String getName() {
        return principal == null ? "" : principal.getUsername();
    }
}
