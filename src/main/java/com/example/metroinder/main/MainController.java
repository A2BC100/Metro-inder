package com.example.metroinder.main;

import com.example.metroinder.user.JwtToken.service.JwtService;
import com.example.metroinder.user.service.OAuth2LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;


@Slf4j
@RequiredArgsConstructor
@Controller
public class MainController {
    private final JwtService jwtService;
    @GetMapping("/")
    public String mainAccess() {
        return "index";
    }

    @GetMapping("/tokenRequestTest")
    public String tokenRequestTest() {
        return "tokenTest";
    }

}

