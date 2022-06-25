package com.example.metroinder.controller;

import com.example.metroinder.service.RealtimeStationservice;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;


@Controller
@RequiredArgsConstructor
public class RealtimeStationController {
    private final RealtimeStationservice RTSs;
    @GetMapping("/getRealtimeStation")
    @ResponseBody
    public String getRealtimeStation(@RequestParam("stationName")String Station) throws Exception {
        RTSs.getStation(Station);
        String realtimeJson = RTSs.realtimeStaion();
        return realtimeJson;
    }

}
