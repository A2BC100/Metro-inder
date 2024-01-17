package com.example.metroinder.stationSchedule.controller;

import com.example.metroinder.stationSchedule.service.StationScheduleService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@AllArgsConstructor
@RestController
public class StationScheduleController {
    private final StationScheduleService stationScheduleService;

    @PostMapping("/returnSchedule")
    @ResponseBody
    public ResponseEntity returnStationSchedule(@RequestParam("stationName") String stationName) throws Exception {

        Map json = stationScheduleService.findStationSchedule(stationName);
        return new ResponseEntity(json, HttpStatus.OK);
    }
}
