package com.example.metroinder.controller;


import com.example.metroinder.model.Station;
import com.example.metroinder.model.TimeStationPersonnel;
import com.example.metroinder.repository.TimeStationPersonnelRepository;
import com.example.metroinder.service.StationFinderService;
import com.example.metroinder.service.TimeStationPersonnelService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Map;

@Controller
@AllArgsConstructor
public class TimeStationPersonnelController {
    private final TimeStationPersonnelService timeStationPersonnelService;

    @GetMapping("/seoulSubwayTimeZoneInformationSave")
    @ResponseBody
    public void seoulSubwayTimeZoneInformationSave() throws IOException {
        for(int i = 5; i <= 9; i++ ) {
            timeStationPersonnelService.peopleInformationBySeoulAtTimeSave("20220"+i);
        }
    }


    @GetMapping("/returnPeopleCount")
    @ResponseBody
    public Map returnPeopleCount(@RequestParam("stationName") String stationName) {
        Map json = timeStationPersonnelService.findSameStationPeople(stationName);
        return json;
    }

}