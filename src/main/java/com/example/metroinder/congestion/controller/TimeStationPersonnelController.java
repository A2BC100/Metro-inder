package com.example.metroinder.congestion.controller;
import com.example.metroinder.congestion.service.TimeStationPersonnelService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
;
import java.util.Map;

@Slf4j
@Controller
@AllArgsConstructor
public class TimeStationPersonnelController {
    private final TimeStationPersonnelService timeStationPersonnelService;

    @GetMapping("/returnPeopleCount")
    @ResponseBody
    public ResponseEntity<Object> returnPeopleCount(@RequestParam("stationName") String stationName) {
        Map<String, Object> json = timeStationPersonnelService.findSameStationPeople(stationName);
        return new ResponseEntity<>(json, HttpStatus.OK);
    }

}