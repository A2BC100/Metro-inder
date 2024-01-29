package com.example.metroinder.congestion.controller;
import com.example.metroinder.congestion.model.PopularTimeInfo;
import com.example.metroinder.congestion.service.PopularTimeService;
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
    private final PopularTimeService popularTimeService;

    @GetMapping("/returnPeopleCount")
    @ResponseBody
    public ResponseEntity<Object> returnPeopleCount(@RequestParam("stationName") String stationName) {
        Map<String, Object> json = timeStationPersonnelService.findSameStationPeople(stationName);
        return new ResponseEntity<>(json, HttpStatus.OK);
    }

    @GetMapping("/test")
    public ResponseEntity<Object> returnPopularTimeData(/*@RequestParam("stationName") String stationName*/) {
        PopularTimeInfo popularTimeInfo = popularTimeService.downloadCurrentTraffic("명학역");
        if (popularTimeInfo != null){
            log.info("역이름 : " + popularTimeInfo.station);
            log.info("시간대 : " + popularTimeInfo.time);
            log.info("붐빔도 : " + popularTimeInfo.peoplerate);
        }
        popularTimeInfo = popularTimeService.downloadCurrentTraffic("금정역");
        if (popularTimeInfo != null){
            log.info("역이름 : " + popularTimeInfo.station);
            log.info("시간대 : " + popularTimeInfo.time);
            log.info("붐빔도 : " + popularTimeInfo.peoplerate);
        }
        popularTimeInfo = popularTimeService.downloadCurrentTraffic("구로역");
        if (popularTimeInfo != null){
            log.info("역이름 : " + popularTimeInfo.station);
            log.info("시간대 : " + popularTimeInfo.time);
            log.info("붐빔도 : " + popularTimeInfo.peoplerate);
        }
        popularTimeInfo = popularTimeService.downloadCurrentTraffic("구파발역");
        if (popularTimeInfo != null){
            log.info("역이름 : " + popularTimeInfo.station);
            log.info("시간대 : " + popularTimeInfo.time);
            log.info("붐빔도 : " + popularTimeInfo.peoplerate);
        }
        popularTimeInfo = popularTimeService.downloadCurrentTraffic("안양역");
        if (popularTimeInfo != null){
            log.info("역이름 : " + popularTimeInfo.station);
            log.info("시간대 : " + popularTimeInfo.time);
            log.info("붐빔도 : " + popularTimeInfo.peoplerate);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}