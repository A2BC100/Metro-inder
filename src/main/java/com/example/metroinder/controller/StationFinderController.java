package com.example.metroinder.controller;

import com.example.metroinder.model.Station;
import com.example.metroinder.model.StationLine;
import com.example.metroinder.model.TimeStationPersonnel;
import com.example.metroinder.service.StationFinderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
public class StationFinderController {
    private final StationFinderService stationFinderService;

    /*@GetMapping("/test2")
    public void testGetDirections()  {
        stationFinderService.stationAllListSet();
        stationFinderService.find(stationFinderService., "신길");
        List<Station> findLoad = stationFinderService.getOpenList();
        for(Station station : findLoad) {
            log.info(station.getName());
        }
    }*/
}
