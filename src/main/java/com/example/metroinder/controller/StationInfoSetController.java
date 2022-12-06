package com.example.metroinder.controller;

import com.example.metroinder.service.StationInformationSetService;
import com.example.metroinder.service.StationScheduleService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;


import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
public class StationInfoSetController {
    private final StationInformationSetService stationInformationSetService;
    private final StationScheduleService stationScheduleService;

    @GetMapping("/setInfo")
    public ResponseEntity setInfo() throws IOException, ParseException, NullPointerException {
        log.info("데이터 저장중...");
        stationInformationSetService.setStationInformation();
        stationInformationSetService.setLetLon();
        List<String> stationList = stationScheduleService.stationDistinctList();

        /* 오래걸려서 일단 주석처리 */
        for(int i = 0; i < stationList.size(); i++) {
            //log.info("역이름 테스트("+ i + ") : " +stationList.get(i));
            String json = stationScheduleService.getStationCode(stationList.get(i));
            stationScheduleService.stationScheduleCall(json, "1", "1");
            stationScheduleService.stationScheduleCall(json, "1", "2");
            stationScheduleService.stationScheduleCall(json, "2", "1");
            stationScheduleService.stationScheduleCall(json, "2", "2");
            stationScheduleService.stationScheduleCall(json, "3", "1");
            stationScheduleService.stationScheduleCall(json, "3", "2");

        }
        stationInformationSetService.getStationDegreeOfCongestionAvg();
        log.info("데이터 저장완료");
        return new ResponseEntity(HttpStatus.OK);
    }
}
