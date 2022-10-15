package com.example.metroinder.controller;

import com.example.metroinder.service.StationScheduleService;
import com.example.metroinder.service.TimeStationPersonnelService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;

@Slf4j
@Controller
@AllArgsConstructor
public class StationScheduleController {
    private final StationScheduleService stationScheduleService;
    private final TimeStationPersonnelService timeStationPersonnelService;


    @GetMapping("/getStationSchedule")
    @ResponseBody
    public void stationScheduler() throws IOException {
        // 우선 혼잡도데이터 DB에서 겹치지 않는(distint) 역이름을 list에 담아오고
        List<String> stationList = timeStationPersonnelService.stationDistinctList();

        for(int i = 0; i < stationList.size(); i++) {
            //log.info("역이름 테스트("+ i + ") : " +stationList.get(i));
            String json = stationScheduleService.getStationCode(stationList.get(i));
            stationScheduleService.stationScheduleCall(json, "1", "1");
            //stationScheduleService.setStationScheduleSave(json, "1", "2");
            //stationScheduleService.setStationScheduleSave(json, "2", "1");
            //stationScheduleService.setStationScheduleSave(json, "2", "2");
            //stationScheduleService.setStationScheduleSave(json, "3", "1");
            //stationScheduleService.setStationScheduleSave(json, "3", "2");
            /*for(int week = 1; week <= 3; week++) {
                for(int inOut = 1; inOut <= 2; week++) {
                    stationScheduleService.stationScheduleCall(json, ""+week, ""+inOut);
                }
            }*/
        }

        // 여기서 역이름으로 역코드 api 호출 시 호선마다 나올 수 있어 데이터가 여러개므로, DB에는 호선 정보를 새로 저장해야하고 역코드 수 만큼 반복해서 역시간표를 저장해줘야함
        // 검색 시 반환 코드도 생각을 해봐야할듯함.
        //String json = stationScheduleService.getStationSchduleAPI("0245", "1", "1");
    }
}
