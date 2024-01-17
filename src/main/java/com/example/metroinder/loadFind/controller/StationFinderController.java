package com.example.metroinder.loadFind.controller;

import com.example.metroinder.dataSet.model.Station;
import com.example.metroinder.dataSet.repository.StationLineRepository;
import com.example.metroinder.loadFind.service.StationFinderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;


@Slf4j
@RestController
@AllArgsConstructor
public class StationFinderController {
    private final StationFinderService stationFinderService;
    private final StationLineRepository stationLineRepository;

    @GetMapping("/test")
    //@ResponseBody
    //ResponseEntity
    public void testGetDirections() throws NullPointerException {
        stationFinderService.stationAllListSet();

        Station curStn = stationFinderService.getStation("서울");//안양
        Station destStn = stationFinderService.getStation("신도림");//천안
        /*Station stn1 = stationFinderService.getStation("남영");
        Station stn2 = stationFinderService.getStation("용산");
        Station stn3 = stationFinderService.getStation("노량진");
        Station stn4 = stationFinderService.getStation("대방");
        Station stn5 = stationFinderService.getStation("신길");
        Station stn6 = stationFinderService.getStation("영등포");
        Station stn7 = stationFinderService.getStation("신도림");*/

        log.info("시작역 : " + curStn.getName());
        /*for (Station station : stn1.getNext()) {
            log.info(stn1.getName() + "의 다음역 : " + station.getName());
        }
        for (Station station : stn2.getNext()) {
            log.info(stn2.getName() + "의 다음역 : " + station.getName());
        }
        for (Station station : stn3.getNext()) {
            log.info(stn3.getName() + "의 다음역 : " + station.getName());
        }
        for (Station station : stn4.getNext()) {
            log.info(stn4.getName() + "의 다음역 : " + station.getName());
        }
        for (Station station : stn5.getNext()) {
            log.info(stn5.getName() + "의 다음역 : " + station.getName());
        }
        for (Station station : stn6.getNext()) {
            log.info(stn6.getName() + "의 다음역 : " + station.getName());
        }
        for (Station station : stn7.getNext()) {
            log.info(stn7.getName() + "의 다음역 : " + station.getName());
        }*/



        List<Station> loadList = stationFinderService.find(curStn, destStn);

        for (Station station : loadList) {
            log.info("테스트 log : " +  station.getName());
        }
    }
    /*@GetMapping("/returnLoad")
    @ResponseBody
    public ResponseEntity returnPeopleCount(@RequestParam("stationName") String stationName) {
        stationFinderService.stationAllListSet();
        stationFinderService.find(stationFinderService., "신길");
        List<Station> findLoad = stationFinderService.getOpenList();
        for(Station station : findLoad) {
            log.info(station.getName());
        }
        return new ResponseEntity(json, HttpStatus.OK);
    }*/
}
