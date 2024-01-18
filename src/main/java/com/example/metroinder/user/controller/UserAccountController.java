package com.example.metroinder.user.controller;

import com.example.metroinder.user.JwtToken.service.JwtService;
import com.example.metroinder.user.model.UserAccount;
import com.example.metroinder.user.service.OAuth2LoginService;
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
    public ResponseEntity<Object> loginMetroinder(@RequestParam("code") String code, @RequestParam(value = "provider", required = false) String provider, @RequestParam(value = "state"/*, required = false*/) String state) {
        //provider = "NAVER";
        provider = provider.toLowerCase();
        log.info("naver code : " + code);
        log.info("naver state : " + state);
        String snsAccessToken = oAuth2LoginService.getSnsAccessToken(code, provider, state);

        Map<String, String> userInfo = oAuth2LoginService.getUserInfo(provider, snsAccessToken);
        UserAccount userAccount = oAuth2LoginService.saveUser(userInfo);

        String accessToken = jwtService.createAccessTokens(userAccount);
        String refreshToken = jwtService.createRefreshTokens(userAccount);
        oAuth2LoginService.updateRefreshToken(userAccount, refreshToken);
        //response header 세팅

        MultiValueMap<String, String> loginResponseHeader = new LinkedMultiValueMap<>();
        jwtService.loginResponseHeader(loginResponseHeader, accessToken, refreshToken);

        // response Body 세팅
        Map<String, String> loginResponseBody = new HashMap<>();
        jwtService.loginResponseBody(loginResponseBody, userAccount);

        return new ResponseEntity<>(loginResponseBody, loginResponseHeader, HttpStatus.OK);
        //return new ResponseEntity<>(HttpStatus.OK); //test 용
    }
}
