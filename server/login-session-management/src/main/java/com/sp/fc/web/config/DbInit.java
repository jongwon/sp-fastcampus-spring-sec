package com.sp.fc.web.config;

import com.sp.fc.user.domain.SpUser;
import com.sp.fc.user.service.SpUserService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DbInit implements InitializingBean {

    @Autowired
    private SpUserService userService;

    @Override
    public void afterPropertiesSet() throws Exception {
        if(!userService.findUser("user1").isPresent()){
            SpUser user = userService.save(SpUser.builder()
                    .email("user1")
                    .password("1111")
                    .enabled(true)
                    .build());
            userService.addAuthority(user.getUserId(), "ROLE_USER");
        }
        if(!userService.findUser("user2").isPresent()){
            SpUser user = userService.save(SpUser.builder()
                    .email("user2")
                    .password("1111")
                    .enabled(true)
                    .build());
            userService.addAuthority(user.getUserId(), "ROLE_USER");
        }
        if(!userService.findUser("admin").isPresent()){
            SpUser user = userService.save(SpUser.builder()
                    .email("admin")
                    .password("1111")
                    .enabled(true)
                    .build());
            userService.addAuthority(user.getUserId(), "ROLE_ADMIN");
        }


    }



}
