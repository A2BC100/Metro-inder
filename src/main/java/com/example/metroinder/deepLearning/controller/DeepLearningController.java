package com.example.metroinder.deepLearning.controller;

import com.example.metroinder.deepLearning.service.DeepLearningService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.File;

@Slf4j
@RequiredArgsConstructor
@Controller
public class DeepLearningController {
    private final DeepLearningService deepLearningService;

    @GetMapping("test")
    public void test() {
        /* 모델 학습 후 저장 */
        //deepLearningService.configureHyperparameters();
        deepLearningService.trainModel();

//        /* 특정 호선과 특정 역에 대한 예측을 위한 입력 데이터 생성 */
//        String targetLine = "1"; //임시
//        String targetStation = "서울역"; //임시
//        String targetDate = "2018-12-30"; //YYYY-MM-DD 형식
//
//        int targetStationNumber = deepLearningService.findStationNumber(targetStation,targetLine);
//
//        /* 모델을 로딩하고 임시 데이터로 예측 시행, 예측 데이터의 다음일의 시간대별 데이터가 나올 것으로 추정 */
//        deepLearningService.predict(targetStationNumber, targetDate);
    }
}
