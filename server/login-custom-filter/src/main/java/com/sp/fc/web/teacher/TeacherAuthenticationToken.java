package com.sp.fc.web.teacher;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TeacherAuthenticationToken implements Authentication {

    private String credentials; // id
    private Set<GrantedAuthority> authorities;
    private Teacher principal;
    private boolean authenticated;
    private String details;

    @Override
    public String getName() {
        return principal == null ? "" : principal.getUsername();
    }
}
