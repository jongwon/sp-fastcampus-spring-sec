package com.sp.fc.web.config;

import com.sp.fc.user.domain.SpAuthority;
import com.sp.fc.user.domain.SpUser;
import com.sp.fc.user.repository.SpUserRepository;
import com.sp.fc.user.service.SpUserService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class DbInit implements InitializingBean {

    @Autowired
    private SpUserService userService;

    @Override
    public void afterPropertiesSet() throws Exception {
        if(!userService.findUser("user@test.com").isPresent()){
            SpUser user = userService.save(SpUser.builder()
                    .email("user@test.com")
                    .password("1111")
                    .enabled(true)
                    .build());
            userService.addAuthority(user.getUserId(), "ROLE_USER");
        }


    }



}
