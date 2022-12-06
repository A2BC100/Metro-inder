package com.example.metroinder.controller;


import com.example.metroinder.model.Station;
import com.example.metroinder.model.TimeStationPersonnel;
import com.example.metroinder.repository.TimeStationPersonnelRepository;
import com.example.metroinder.service.StationFinderService;
import com.example.metroinder.service.TimeStationPersonnelService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Controller
@AllArgsConstructor
public class TimeStationPersonnelController {
    private final TimeStationPersonnelService timeStationPersonnelService;

    @GetMapping("/seoulSubwayTimeZoneInformationSave")
    @ResponseBody
    public void seoulSubwayTimeZoneInformationSave() throws IOException {
        log.info("혼잡도 데이터 저장중...");
        for(int i = 5; i <= 9; i++ ) {
            timeStationPersonnelService.peopleInformationBySeoulAtTimeSave("20220"+i);
        }
        log.info("혼잡도 데이터 저장완료");
    }


    @GetMapping("/returnPeopleCount")
    @ResponseBody
    public ResponseEntity returnPeopleCount(@RequestParam("stationName") String stationName) {
        Map json = timeStationPersonnelService.findSameStationPeople(stationName);
        System.out.println(json);
        return new ResponseEntity(json, HttpStatus.OK);
    }

}