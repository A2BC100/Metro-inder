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
       JSONObject realtimeStaion = realtimeStationservice.realtimeStaion(station);
        return new ResponseEntity(realtimeStaion,HttpStatus.OK);
    }
    @GetMapping("/weather")
    @ResponseBody
    public ResponseEntity getRealtimeWeather(@RequestParam ("lat")double lat,@RequestParam("lon") double lon,@RequestParam("date")String date,@RequestParam("time")String time) throws Exception {

        JSONObject realtiemWeather = realtimeStationservice.realtiemWeather(lat,lon,date,time);
        return new ResponseEntity(realtiemWeather,HttpStatus.OK);
    }

}
