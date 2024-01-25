package com.example.metroinder.user.controller;

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
    public ResponseEntity<Object> validationAccess(@RequestHeader HttpHeaders header) {
        log.info("validationAccess 호출됨");
        log.info(header.toString());

        MultiValueMap<String, String> responseHeader = new LinkedMultiValueMap<>();
        responseHeader.add("Content-type", "application/json;charset=utf-8");

        Map<String, String> responseBody = new HashMap<>();
        if(jwtService.tokenValitaion("a", header, responseBody)) {
            if("mtv_rc_1".equals(responseBody.get("validationResult"))) {
                return new ResponseEntity<>(responseBody, responseHeader, HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(responseBody, responseHeader, HttpStatus.OK);
    }

    @GetMapping("/validationRefresh")
    @ResponseBody
    public ResponseEntity<Object> validationRefresh(@RequestHeader HttpHeaders headers) {
        log.info("validationAccess 호출됨");
        log.info(headers.toString());

        MultiValueMap<String, String> responseHeader = new LinkedMultiValueMap<>();
        responseHeader.add("Content-type", "application/json;charset=utf-8");

        Map<String, String> responseBody = new HashMap<>();
        if(jwtService.tokenValitaion("r", headers, responseBody)) {
            if("mtv_rc_1".equals(responseBody.get("validationResult"))) {
                return new ResponseEntity<>(responseBody, responseHeader, HttpStatus.BAD_REQUEST);
            }
        }

        jwtService.updateAccessToken(headers, responseHeader);
        return new ResponseEntity<>(responseBody, responseHeader, HttpStatus.OK);
    }
}
