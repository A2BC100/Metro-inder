package com.example.metroinder.realtime.controller;

import com.example.metroinder.realtime.dto.request.RealTimeStationRequest;
import com.example.metroinder.realtime.dto.request.RealTimeWeatherRequest;
import com.example.metroinder.realtime.service.RealtimeStationservice;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor
@RequestMapping("api/realtime")
public class RealtimeController {
    private final RealtimeStationservice realtimeStationservice;
    @GetMapping("/station")
    @ResponseBody
    public ResponseEntity getRealtimeStation(@RequestBody RealTimeStationRequest realTimeStationRequest) throws Exception {
       String realtimeJson = realtimeStationservice.realtimeStaion(realTimeStationRequest);
        return new ResponseEntity(realtimeJson,HttpStatus.OK);
    }
    @GetMapping("/weather")
    public ResponseEntity getRealtimeWeather(@RequestBody RealTimeWeatherRequest realTimeWeatherResquest) throws Exception {

        return new ResponseEntity(HttpStatus.OK);
    }

}
