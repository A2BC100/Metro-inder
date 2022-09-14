package com.example.metroinder.controller;


import com.example.metroinder.service.TimeStationPersonnelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class TimeStationPersonnelController {
    private final TimeStationPersonnelService timeStationPersonnelService;

    @GetMapping("/seoulSubwayTimeZoneInformationSave")
    @ResponseBody
    public void seoulSubwayTimeZoneInformationSave() throws IOException {
        for(int i = 5; i <= 7; i++ ) {
            String json = timeStationPersonnelService.peopleInformationBySeoulAtTimeRead("20220"+i);
            timeStationPersonnelService.peopleInformationBySeoulAtTimeSave(json);
        }
    }


    @GetMapping("/returnPeopleCount")
    @ResponseBody
    public Map returnPeopleCount(@RequestParam("stationName") String stationName) {
        Map json = timeStationPersonnelService.findSameStationPeople(stationName, 3);
        return json;
    }
}