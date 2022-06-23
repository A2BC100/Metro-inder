package com.example.demo.controller;

import com.example.demo.service.ApiService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
@AllArgsConstructor
public class ApiDataSaveController {
    ApiService apiService;
    @GetMapping("/test")
    public String test(Model model) throws IOException {
        apiService.peopleInformationBySeoulAtTime(); //- 결과 확인 O
        //jsonReader.trainArrivalTnformation(); //- 실시간 인증키가 나오기 전까지는 사용 및 확인 불가
        return "test";
    }
}
