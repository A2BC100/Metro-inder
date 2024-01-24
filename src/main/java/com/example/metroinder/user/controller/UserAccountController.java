package com.example.metroinder.user.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.metroinder.user.JwtToken.service.JwtService;
import com.example.metroinder.user.model.UserAccount;
import com.example.metroinder.user.service.OAuth2LoginService;
import com.nimbusds.oauth2.sdk.token.AccessToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Slf4j
@Controller
@RequiredArgsConstructor
public class UserAccountController {

    private final OAuth2LoginService oAuth2LoginService;
    private final JwtService jwtService;

    //@GetMapping("/auth/kakao/callback") //test 용
    //@GetMapping("/login/oauth2/code/google") //test 용
    //@GetMapping("/login/oauth2/code/naver") //test 용
    @GetMapping("/loginMetroinder")
    @ResponseBody
    public ResponseEntity<Object> loginMetroinder(@RequestParam("code") String code, @RequestParam(value = "provider", required = false) String provider, @RequestParam(value = "state", required = false) String state) {
        try {
            //provider = "KAKAO";
            provider = provider.toLowerCase();
            log.info("/loginMetroinder 호출, AccessToken 발급 전");
            String snsAccessToken = oAuth2LoginService.getSnsAccessToken(code, provider, state);
            log.info("AccessToken 발급 후");
            Map<String, String> userInfo = oAuth2LoginService.getUserInfo(provider, snsAccessToken);
            log.info("user 프로필 발급 후");
            UserAccount userAccount = oAuth2LoginService.saveUser(userInfo);
            log.info("user 정보 저장 후");
            String refreshToken = jwtService.createRefreshTokens(userAccount);

            String accessToken = jwtService.createAccessTokens(userAccount);

            oAuth2LoginService.updateRefreshToken(userAccount, refreshToken);
            //response header 세팅
            log.info("토큰 발급 후");
            MultiValueMap<String, String> loginResponseHeader = new LinkedMultiValueMap<>();
            jwtService.loginResponseHeader(loginResponseHeader, accessToken, refreshToken);

            // response Body 세팅
            Map<String, String> loginResponseBody = new HashMap<>();
            jwtService.loginResponseBody(loginResponseBody, userAccount);

            log.info(loginResponseHeader.toString());
            log.info(loginResponseBody.toString());

            return new ResponseEntity<>(loginResponseBody, loginResponseHeader, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/validationAccess")
    @ResponseBody
    public ResponseEntity<Object> validationAccess(@RequestHeader(value="Authorization") String accessToken) {
        log.info(accessToken);
        Optional.ofNullable(accessToken)
                .filter(refreshToken -> accessToken.startsWith("Bearer"))
                .map(refreshToken -> accessToken.replace("Bearer ", ""));
        log.info(accessToken);
        String secretKey = "c3ByaW5nLWJvb3Qtc2VjdXJpdHktand0LXR1dG9yaWFsLWppd29vbi1zcHJpbmctYm9vdC1zZWN1cml0eS1qd3QtdHV0b3JpYWwK";
        try {
            Optional.ofNullable(JWT.require(Algorithm.HMAC512(secretKey))
                    .build() // 반환된 빌더로 JWT verifier 생성
                    .verify(accessToken) // accessToken을 검증하고 유효하지 않다면 예외 발생
                    .getClaim("aud") // 유저정보 가져오기
                    .asString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
