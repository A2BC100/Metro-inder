package com.example.metroinder.user.JwtToken.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.metroinder.user.model.UserAccount;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Getter
public class JwtService {
    private final String AUTHORITIES_KEY = "auth";
    private final String BEARER_TYPE = "Bearer";

    @Value("${jwt.access.expiration}")
    private Long ACCESS_TOKEN_EXPIRE_TIME;

    @Value("${jwt.refresh.expiration}")
    private Long REFRESH_TOKEN_EXPIRE_TIME;

    @Value("${jwt.access.header}")
    private String accessHeader;

    @Value("${jwt.refresh.header}")
    private String refreshHeader;

    @Value("${jwt.secretKey}")
    private String secretKey;

    // 유저 정보로 AccessToken 생성
    public String createAccessTokens(UserAccount user) {
        Date now = new Date();
        return JWT.create() // JWT 토큰을 생성하는 빌더 반환
                .withSubject("AccessToken") // JWT의 Subject 지정
                .withExpiresAt(new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME)) // 토큰 만료 시간 설정
                .withClaim("iss", "metroinder.co.kr")
                .withClaim("aud", user.getUsername())
                .withClaim("iat",new Date(now.getTime()))
                .withClaim("provider", user.getProvider())
                .sign(Algorithm.HMAC512(secretKey)); // HMAC512 알고리즘 사용
    }

    // RefreashToken 생성
    public String createRefreshTokens(UserAccount user) {
        Date now = new Date();
        return JWT.create()
                .withSubject("RefreshToken")
                .withExpiresAt(new Date(now.getTime() + REFRESH_TOKEN_EXPIRE_TIME))
                .withClaim("iss", "metroinder.co.kr")
                .withClaim("aud", user.getUsername())
                .withClaim("iat",new Date(now.getTime()))
                .withClaim("provider", user.getProvider())
                .sign(Algorithm.HMAC512(secretKey));
    }


    // AccessToken 헤더에 실어서 보내기
    public void sendAccessToken(HttpServletResponse response, String accessToken) {
        response.setStatus(HttpServletResponse.SC_OK);
        //response.setHeader("UserName", );
        response.setHeader(accessHeader, accessToken);
        log.info("재발급된 Access Token : {}", accessToken);
    }


    public void sendAccessAndRefreshToken(HttpServletResponse response, UserAccount user, String accessToken, String refreshToken) {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader("UserName", user.getUsername());
        response.setHeader("email", user.getEmail());
        response.setHeader(accessHeader, accessToken);
        response.setHeader(refreshHeader, refreshToken);
    }

    // 헤더에서 RefreshToken 추출
    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        /*String refreshToken = request.getHeader(refreshHeader);
        if(refreshToken != null) {
            if(refreshToken.startsWith("Bearer")) {
                refreshToken.replace("Bearer", "");
            }
        }
        return refreshToken;*/
        return Optional.ofNullable(request.getHeader(refreshHeader))
                .filter(refreshToken -> refreshToken.startsWith("Bearer"))
                .map(refreshToken -> refreshToken.replace("Bearer ", ""));
    }

    // 헤더에서 AccessToken 추출
    public Optional<String> extractAccessToken(HttpServletRequest request) {
        /*String accessToken = request.getHeader(accessHeader);
        if(accessToken != null) {
            if(accessToken.startsWith("Bearer")) {
                accessToken.replace("Bearer", "");
            }
        }
        return accessToken;*/
        return Optional.ofNullable(request.getHeader(accessHeader))
                .filter(refreshToken -> refreshToken.startsWith("Bearer"))
                .map(refreshToken -> refreshToken.replace("Bearer ", ""));
    }

    // AcessToken에서 유저정보 추출
    public Optional<String> extractUser(String accessToken) {
        try {
            // 토큰 유효성 검사하는 데에 사용할 알고리즘이 있는 JWT verifier builder 반환
            return Optional.ofNullable(JWT.require(Algorithm.HMAC512(secretKey))
                    .build() // 반환된 빌더로 JWT verifier 생성
                    .verify(accessToken) // accessToken을 검증하고 유효하지 않다면 예외 발생
                    .getClaim("aud") // 유저정보 가져오기
                    .asString());
        } catch (Exception e) {
            log.error("액세스 토큰이 유효하지 않습니다.");
            return Optional.empty();
        }
    }

    public boolean isTokenValid(String token) {
        try {
            JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token);
            return true;
        } catch (Exception e) {
            log.error("유효하지 않은 토큰입니다. {}", e.getMessage());
            return false;
        }
    }

    public void loginResponseHeader(MultiValueMap<String, String> headers, String accessToken, String refreshToken) {
        headers.add("Content-type", "application/json;charset=utf-8");
        headers.add(accessHeader, accessToken);
        headers.add(refreshHeader, refreshToken);
    }
    public void loginResponseBody(Map<String, String> map, UserAccount user) {
        map.put("UserName", user.getUsername());
        map.put("email", user.getEmail());
    }
}
