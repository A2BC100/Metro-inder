package com.example.metroinder.realtime.controller;

import com.example.metroinder.realtime.dto.request.RealTimeRequest;
import com.example.metroinder.realtime.service.RealtimeStationservice;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequiredArgsConstructor
public class RealtimeController {
    private final RealtimeStationservice realtimeStationservice;
    @GetMapping("/getRealtimeStation")
    @ResponseBody
    public ResponseEntity getRealtimeStation(@RequestParam RealTimeRequest.RealTimeStationRequest realTimeStationRequest) throws Exception {
        realtimeStationservice.getStation(realTimeStationRequest);
        return new ResponseEntity(HttpStatus.OK);
    }

}
