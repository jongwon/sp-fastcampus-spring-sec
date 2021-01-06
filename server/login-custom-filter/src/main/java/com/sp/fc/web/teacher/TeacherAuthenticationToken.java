package com.sp.fc.web.teacher;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TeacherAuthenticationToken implements Authentication {

    private String credentials; // id
    private final Set<GrantedAuthority> authorities = Set.of(new SimpleGrantedAuthority("ROLE_TEACHER"));
    private Teacher principal;
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
