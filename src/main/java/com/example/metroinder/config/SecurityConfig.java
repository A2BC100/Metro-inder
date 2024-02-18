package com.example.metroinder.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .httpBasic().disable()// basic auth 미사용
                .csrf().disable() // csrf 보안 미사용
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);//JWT 사용으로 세션 미사용//세션이 미사용되면서 SpringSecurity에는 데이터가 남지 않게됨
                /*.and()
                .oauth2Login()
                .userInfoEndpoint()// 로그인 성공 후 사용자 정보를 가져옴//JWT 사용으로 세션 미사용//세션이 미사용되면서 SpringSecurity에는 데이터가 남지 않게됨
                .userService(principalOauth2UserService)// userInfoEndpoint()로 가져온 사용자 정보를 처리할 때 사용//JWT 사용으로 세션 미사용//세션이 미사용되면서 SpringSecurity에는 데이터가 남지 않게됨
                .and()
                .successHandler(oAuth2LoginSuccessHandler)// 로그인 성공 시 handle
                .failureHandler(oAuth2LoginFailureHandler);*/

        //http.addFilterAfter(, LogoutFilter.class);
        //http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class); // JWT 인증을 위하여 직접 구현한 필터를 UsernamePasswordAuthenticationFilter 전에 실행

    }
}