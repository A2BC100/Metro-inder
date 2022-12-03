package com.example.metroinder.realtime.controller;

import com.example.metroinder.realtime.dto.request.RealTimeWeatherRequest;
import com.example.metroinder.realtime.service.RealtimeStationservice;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/realtime")
public class RealtimeController {
    private final RealtimeStationservice realtimeStationservice;
    @GetMapping("/station")
    @ResponseBody
    public ResponseEntity getRealtimeStation(@RequestParam("stationName") String station) throws Exception {
       JSONObject realtimeJson = realtimeStationservice.realtimeStaion(station);
        return new ResponseEntity(realtimeJson,HttpStatus.OK);
    }
    @GetMapping("/weather")
    @ResponseBody
    public ResponseEntity getRealtimeWeather(@RequestBody RealTimeWeatherRequest realTimeWeatherResquest) throws Exception {

        return new ResponseEntity(HttpStatus.OK);
    }

}
