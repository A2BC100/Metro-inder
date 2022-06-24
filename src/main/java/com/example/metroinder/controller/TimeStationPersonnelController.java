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

    @PostMapping("/seoulSubwayTimeZoneInformationSave")
    @ResponseBody
    public void seoulSubwayTimeZoneInformationSave() throws IOException {
        String json = timeStationPersonnelService.peopleInformationBySeoulAtTimeRead();
        timeStationPersonnelService.peopleInformationBySeoulAtTimeSave(json);
    }


    @GetMapping("/returnPeopleCount")
    @ResponseBody
    public Map returnPeopleCount(@RequestParam("stationName") String stationName) {
        Map json = timeStationPersonnelService.findSameStationPeople(stationName);
        return json;
    }
}