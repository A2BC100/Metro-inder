package com.example.metroinder.user.JwtToken.filter;

import com.example.metroinder.user.JwtToken.service.JwtService;
import com.example.metroinder.user.model.UserAccount;
import com.example.metroinder.user.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;



@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserAccountRepository userAccountRepository;

    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    // 실제 필터링 로직은 doFilterInternal 에 들어감
    // JWT 토큰의 인증 정보를 현재 쓰레드의 SecurityContext 에 저장하는 역할 수행
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("Request url : " + request.getRequestURI());

        /*String att = request.getHeader("Authorization");
        String rtt = request.getHeader("Authorization-refresh");
        if(att != null) {
            log.info("AcessToken : " + att);
        }
        if(rtt != null) {
            log.info("refreshToken : " + rtt);
        }*/

        String refreshToken = jwtService.extractRefreshToken(request)
                .filter(jwtService::isTokenValid)
                .orElse(null);

        if (refreshToken != null) {
            checkRefreshTokenAndReIssueAccessToken(request, response, refreshToken);
            filterChain.doFilter(request, response);
            return; // RefreshToken을 보낸 경우에는 AccessToken을 재발급 하고 인증 처리는 하지 않게 하기위해 바로 return으로 필터 진행 막기
        } else {
            checkAccessTokenAndAuthentication(request, response, filterChain);
        }
        filterChain.doFilter(request, response);
    }

    public void checkRefreshTokenAndReIssueAccessToken(HttpServletRequest request,  HttpServletResponse response, String refreshToken) {
        String provider = request.getHeader("provider");
        String email = request.getHeader("email");
        UserAccount user = userAccountRepository.findByProviderAndEmail(provider, email);
        user.setRefreshToken(refreshToken);
        userAccountRepository.save(user);
        jwtService.sendAccessAndRefreshToken(response, user, jwtService.createAccessTokens(user), jwtService.createRefreshTokens(user));
    }
    public void checkAccessTokenAndAuthentication(HttpServletRequest request, HttpServletResponse response,
                                                  FilterChain filterChain) throws ServletException, IOException {
        String provider = request.getHeader("provider");
        String email = request.getHeader("email");
        if(provider == null || email == null) {
            //log.info("리프레시 토큰은 전달되지 않았으며, request header에 유저정보가 존재하지 않습니다.");
            return;
        } else {
            UserAccount findUser = userAccountRepository.findByProviderAndEmail(provider, email);
            if(findUser == null) {
                //log.info("리프레시 토큰은 전달되지 않았으며, DB에 해당하는 유저정보가 존재하지 않습니다.");
                return;
            } else {
                //log.info("리프레시 토큰은 전달되지 않았으며, DB에 해당 유저정보가 존재합니다. userName : " + findUser.getUsername());
                String acessToken = jwtService.extractAccessToken(request)
                        .filter(jwtService::isTokenValid)
                        .orElse(null);
                if (acessToken != null) {
                    saveAuthentication(findUser);
                    filterChain.doFilter(request, response);
                } else {
                    //response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
            }
        }

    }

    public void saveAuthentication(UserAccount userAccount) {
        String userRole = userAccount.getRole().name();

        if(userRole.startsWith("ROLE_")) {
            userRole = userRole.substring(5);
        }
        log.info(userRole);
        UserDetails userDetailsUser = org.springframework.security.core.userdetails.User.builder()
                .username(userAccount.getUsername())
                .roles(userRole)
                .build();

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(userDetailsUser, null,
                        authoritiesMapper.mapAuthorities(userDetailsUser.getAuthorities()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
