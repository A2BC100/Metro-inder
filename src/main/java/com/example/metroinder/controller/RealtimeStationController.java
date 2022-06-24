package com.example.metroinder.controller;

import com.example.metroinder.service.RealtimeStationservice;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class RealtimeStationController {
    private final RealtimeStationservice RTSs;
    @GetMapping("/getRealtimeStation")
    public JSONArray getRealtimeStation(@RequestParam("stationName")String Station) throws IOException {
        RTSs.getStation(Station);
        String realtimeJson = RTSs.realtimeStaion();
        JSONArray realtimedata = RTSs.returnRealtimeStatopm(realtimeJson);
        return realtimedata;
    }

}
