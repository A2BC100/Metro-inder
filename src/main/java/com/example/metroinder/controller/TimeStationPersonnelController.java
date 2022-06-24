package com.example.metroinder.controller;


import com.example.metroinder.service.TimeStationPersonnelService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class TimeStationPersonnelController {
    private final TimeStationPersonnelService timeStationPersonnelService;

    @PostMapping("/seoulSubwayTimeZoneInformationSave")
    public void seoulSubwayTimeZoneInformationSave() throws IOException {
        String json = timeStationPersonnelService.peopleInformationBySeoulAtTimeRead();
        timeStationPersonnelService.peopleInformationBySeoulAtTimeSave(json);
    }

    @GetMapping("/returnPeopleCount")
    public JSONArray returnPeopleCount(@RequestParam("stationName") String stationName) {
        JSONArray json = timeStationPersonnelService.findSameStationPeople(stationName);
        System.out.println(json.get(0).toString());
        return json;
    }
}