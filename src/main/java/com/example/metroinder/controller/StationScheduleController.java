package com.example.metroinder.controller;

import com.example.metroinder.repository.StationScheduleRepository;
import com.example.metroinder.service.StationScheduleService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@AllArgsConstructor
@RestController
public class StationScheduleController {
    private final StationScheduleService stationScheduleService;

    @GetMapping("/returnSchedule")
    @ResponseBody
    public ResponseEntity returnStationSchedule(@RequestParam("stationName") String stationName) throws Exception {
        Map json = stationScheduleService.findStationSchedule(stationName);
        return new ResponseEntity(json, HttpStatus.OK);
    }
}
