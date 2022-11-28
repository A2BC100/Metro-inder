package com.example.metroinder.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class MainController {

    @GetMapping("/")
    public String mainAccess() {
        return "index";
    }

    @GetMapping("/testLogin")
    public String snsLoginTest() {
        log.info("로그인용 테스트 페이지 호출 성공?");
        return "test";
    }


}
