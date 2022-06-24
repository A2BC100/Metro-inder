package com.example.metroinder.controller;


import com.example.metroinder.service.TimeStationPersonnelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class TimeStationPersonnelController {
    private final TimeStationPersonnelService timeStationPersonnelService;
    @GetMapping("/seoulSubwayTimeZoneInformationSave")
    public String seoulSubwayTimeZoneInformationSave() throws IOException {
        String json = timeStationPersonnelService.peopleInformationBySeoulAtTimeRead();
        timeStationPersonnelService.peopleInformationBySeoulAtTimeSave(json);
        return "test";
    }
}