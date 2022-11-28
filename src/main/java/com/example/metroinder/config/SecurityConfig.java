package com.example.metroinder.config;

import com.example.metroinder.auth.PrincipalOauth2UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private PrincipalOauth2UserService principalOauth2UserService;

    /*@Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }*/

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http/*.authorizeRequests()
                .antMatchers("/manager/**").access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/admin/**").hasRole("ROLE_ADMIN")
                .anyRequest().permitAll()
                .and()*/
                .oauth2Login()// OAuth2 기반의 로그인 설정
                .defaultSuccessUrl("/")// 로그인 성공 시 이동 url
                .userInfoEndpoint()// 로그인 성공 후 사용자 정보를 가져옴
                .userService(principalOauth2UserService);// userInfoEndpoint()로 가져온 사용자 정보를 처리할 때 사용
    }

    @Bean
    public BCryptPasswordEncoder encodePassword() {
        return new BCryptPasswordEncoder();
    }
}