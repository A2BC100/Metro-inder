package com.example.metroinder.user.JwtToken.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.metroinder.user.model.UserAccount;
import com.example.metroinder.user.repository.UserAccountRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import java.util.Date;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Getter
public class JwtService {
    private final UserAccountRepository userAccountRepository;
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
                .withClaim("aud", user.getEmail())
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
                .withClaim("aud", user.getEmail())
                .withClaim("iat", new Date(now.getTime()))
                .withClaim("provider", user.getProvider())
                .sign(Algorithm.HMAC512(secretKey));
    }

    public void loginResponseHeader(MultiValueMap<String, String> responseHeader, String accessToken, String refreshToken) {
        responseHeader.add("Content-type", "application/json;charset=utf-8");
        responseHeader.add(accessHeader, accessToken);
        responseHeader.add(refreshHeader, refreshToken);
    }
    public void loginResponseBody(Map<String, String> responseBody, UserAccount user) {
        responseBody.put("UserName", user.getUsername());
        responseBody.put("email", user.getEmail());
    }

    public boolean tokenValitaion(String tokenGbn, String token, Map<String, String> responseBody) {
        if (token == null) {
            responseBody.put("validationResult", "mtv_rc_1");
            log.info("token이 존재하지 않음");
            return true;
        }

        if(token.startsWith("Bearer")) {
            token = token.replace("Bearer ", "");
        } else {
            responseBody.put("validationResult", "mtv_rc_2");
            log.info("토큰 요청 시 토큰 타입이 존재하지 않음");
            return true; // 토큰에 Bearer 없음
        }
        try {
            log.info("token : " + token);
            String iss = JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token)
                    .getClaim("iss").asString();
            if(iss == null || !"metroinder.co.kr".equals(iss)) {
                responseBody.put("validationResult", "mtv_rc_2");
                log.info("토큰에 발급처 정보가 일치하지 않음");
                return true; // 토큰 발급처 정보가 없거나, Metroinder 서비스에서 발급한 토큰이 아님
            }
            String aud = JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token)
                    .getClaim("aud").asString();
            String provider = JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token)
                    .getClaim("provider").asString();
            if(aud == null || provider == null) {
                responseBody.put("validationResult", "mtv_rc_2");
                log.info("토큰에 사용자 정보나 소셜 로그인 구분 정보가 존재하지 않음");
                return true; // 토큰에 사용자 정보나 소셜 로그인 구분 정보가 없음
            }
            UserAccount userInfo = userAccountRepository.findByProviderAndEmail(provider, aud);
            if(userInfo == null) {
                responseBody.put("validationResult", "mtv_rc_2");
                log.info("토큰에 유저정보와과 일치하는 유저정보 없음");
                return true; // 토큰에 일치하는 사용자 정보가 없음
            }
            responseBody.put(accessHeader, token);
            responseBody.put(refreshHeader, userInfo.getRefreshToken());
            return false;
        } catch (TokenExpiredException e) {
            responseBody.put("validationResult", "mtv_rc_3");
            log.info("토큰 만료");
            return true; //
        } catch (Exception e) {
            responseBody.put("validationResult", "mtv_rc_1");
            log.error(e.getMessage());
            return true; // 예상치 못한 에러
        }
    }

    public void updateAccessToken(String refreshToken, MultiValueMap<String, String> responseHeader) {
        if(refreshToken == null) {
            return; // warning 때문에 추가
        }
        try {
            if(refreshToken.startsWith("Bearer")) {
                refreshToken = refreshToken.replace("Bearer ", "");
            } else {
                log.info("토큰 요청 시 토큰 타입이 존재하지 않음");
                return; // refresh 토큰에 Bearer 없음
            }
            String aud = JWT.require(Algorithm.HMAC512(secretKey)).build().verify(refreshToken)
                    .getClaim("aud").asString();
            String provider = JWT.require(Algorithm.HMAC512(secretKey)).build().verify(refreshToken)
                    .getClaim("provider").asString();
            UserAccount userInfo = userAccountRepository.findByProviderAndEmail(provider, aud);
            String accessToken = createAccessTokens(userInfo);
            responseHeader.add(accessHeader, accessToken);
            responseHeader.add(refreshHeader, refreshToken);
        }catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
