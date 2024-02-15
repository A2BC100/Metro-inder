package com.example.metroinder.deepLearning.controller;

import com.example.metroinder.deepLearning.service.DeepLearningService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
public class DeepLearningController {
    private final DeepLearningService deepLearningService;

    @GetMapping("test")
    public void test() {

    }
}
