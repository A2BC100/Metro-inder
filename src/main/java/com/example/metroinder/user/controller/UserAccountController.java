package com.example.metroinder.user.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.metroinder.user.JwtToken.service.JwtService;
import com.example.metroinder.user.model.UserAccount;
import com.example.metroinder.user.service.OAuth2LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;


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
            log.info("code : " + code);
            String snsAccessToken = oAuth2LoginService.getSnsAccessToken(code, provider, state);
            Map<String, String> userInfo = oAuth2LoginService.getUserInfo(provider, snsAccessToken);

            UserAccount userAccount = oAuth2LoginService.saveUser(userInfo);

            String refreshToken = jwtService.createRefreshTokens(userAccount);
            String accessToken = jwtService.createAccessTokens(userAccount);

            oAuth2LoginService.updateRefreshToken(userAccount, refreshToken);

            // Response Hdader 세팅
            MultiValueMap<String, String> loginResponseHeader = new LinkedMultiValueMap<>();
            jwtService.loginResponseHeader(loginResponseHeader, accessToken, refreshToken);

            // Response Body 세팅
            Map<String, String> loginResponseBody = new HashMap<>();
            jwtService.loginResponseBody(loginResponseBody, userAccount);

            log.info(loginResponseHeader.toString());
            log.info(loginResponseBody.toString());

            return new ResponseEntity<>(loginResponseBody, loginResponseHeader, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/validationAccess")
    @ResponseBody
    public ResponseEntity<Object> validationAccess(@RequestHeader(value = "Authorization") String token) {
        log.info("validationAccess 호출됨");

        MultiValueMap<String, String> responseHeader = new LinkedMultiValueMap<>();
        responseHeader.add("Content-type", "application/json;charset=utf-8");

        Map<String, String> responseBody = new HashMap<>();
        if(jwtService.tokenValitaion("a", token, responseBody)) {
            if("mtv_rc_1".equals(responseBody.get("validationResult"))) {
                return new ResponseEntity<>(responseBody, responseHeader, HttpStatus.BAD_REQUEST);
            }
        }
        log.info(responseHeader.toString());
        log.info(responseBody.toString());
        return new ResponseEntity<>(responseBody, responseHeader, HttpStatus.OK);
    }

    @GetMapping("/validationRefresh")
    @ResponseBody
    public ResponseEntity<Object> validationRefresh(@RequestHeader(value = "Authorization-refresh") String token ) {
        log.info("validationAccess 호출됨");

        MultiValueMap<String, String> responseHeader = new LinkedMultiValueMap<>();
        responseHeader.add("Content-type", "application/json;charset=utf-8");

        Map<String, String> responseBody = new HashMap<>();
        if(jwtService.tokenValitaion("r", token, responseBody)) {
            if("mtv_rc_1".equals(responseBody.get("validationResult"))) {
                return new ResponseEntity<>(responseBody, responseHeader, HttpStatus.BAD_REQUEST);
            }
        }
        jwtService.updateAccessToken(token, responseHeader);
        log.info(responseHeader.toString());
        log.info(responseBody.toString());
        return new ResponseEntity<>(responseBody, responseHeader, HttpStatus.OK);
    }

    /*@GetMapping("/tokenTest")
    @ResponseBody
    public ResponseEntity<Object> tokenTest() {
        log.info("tokenTest 호출됨");
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBY2Nlc3NUb2tlbiIsImF1ZCI6ImNtczg5MzgxMUBuYXZlci5jb20iLCJwcm92aWRlciI6Imtha2FvIiwiaXNzIjoibWV0cm9pbmRlci5jby5rciIsImV4cCI6MTcwNjIzNDQ1MSwiaWF0IjoxNzA2MjMyNjUxfQ.yQNnIA1W4iBRg7wRCL2TRRY5GC3PV0L0Y_FUUQrPMBozC7se5ZfD4uDZhFA0nFpsp5-dqIPaLqD7x2HQPaFQBQ";
        String secretKey = "c3ByaW5nLWJvb3Qtc2VjdXJpdHktand0LXR1dG9yaWFsLWppd29vbi1zcHJpbmctYm9vdC1zZWN1cml0eS1qd3QtdHV0b3JpYWwK";
        try {
            String iss = JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token)
                    .getClaim("iss").asString();
            if(iss == null || !"metroinder.co.kr".equals(iss)) {
                log.info("토큰에 발급처 정보가 일치하지 않음");
            } else {
                log.info("iss : " + iss);
            }
            String aud = JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token)
                    .getClaim("aud").asString();
            String provider = JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token)
                    .getClaim("provider").asString();
            if(aud == null || provider == null) {
                log.info("토큰에 사용자 정보나 소셜 로그인 구분 정보가 존재하지 않음");
            } else {
                log.info("aud : " + aud);
                log.info("provider : " + provider);
            }
        } catch (TokenExpiredException e) {
            log.info("토큰 만료");
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }*/

}
