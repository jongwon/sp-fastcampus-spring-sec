package com.sp.fc.web.config;

import com.sp.fc.user.domain.SpOauth2User;
import com.sp.fc.user.domain.SpUser;
import com.sp.fc.user.service.SpUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SnsLoginSecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private SpUserService userService;

    @Autowired
    private SpOAuth2UserService oAuth2UserService;

    @Autowired
    private SpOidcUserService oidcUserService;

    @Bean
    PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }

    DaoAuthenticationProvider daoAuthenticationProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
//                .formLogin().and()
                .oauth2Login(
                        oauth2->
                                oauth2.userInfoEndpoint(userInfo->
                                    userInfo.userService(oAuth2UserService)
                                            .oidcUserService(oidcUserService)
                                )
                                        .successHandler(new AuthenticationSuccessHandler() {
                                            @Override
                                            public void onAuthenticationSuccess(
                                                    HttpServletRequest request,
                                                    HttpServletResponse response,
                                                    Authentication authentication
                                            ) throws IOException, ServletException {

                                                Object principal = authentication.getPrincipal();

                                                if(principal instanceof OAuth2User){
                                                    if(principal instanceof OidcUser){
                                                        // google
                                                        SpOauth2User googleUser = SpOauth2User.OAuth2Provider.google.convert((OAuth2User) principal);
                                                        SpUser user = userService.loadUser(googleUser);
                                                        SecurityContextHolder.getContext().setAuthentication(
                                                                new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities())
                                                        );
                                                    }else{
                                                        // naver, or kakao, facebook
                                                        SpOauth2User naverUser = SpOauth2User.OAuth2Provider.naver.convert((OAuth2User) principal);
                                                        SpUser user = userService.loadUser(naverUser);
                                                        SecurityContextHolder.getContext().setAuthentication(
                                                                new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities())
                                                        );
                                                    }
                                                    System.out.println(principal);
                                                    request.getRequestDispatcher("/").forward(request, response);
                                                }

                                            }
                                        })

//                        .and()
//                        .addFilterAfter(userTranslateFilter, OAuth2LoginAuthenticationFilter.class)
                )

                ;
    }
}
