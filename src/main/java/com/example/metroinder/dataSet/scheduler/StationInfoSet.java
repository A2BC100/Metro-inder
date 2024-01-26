package com.example.metroinder.dataSet.scheduler;

import com.example.metroinder.dataSet.repository.StationScheduleRepository;
import com.example.metroinder.dataSet.service.StationInformationSetService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Slf4j
@AllArgsConstructor
@Component
@EnableScheduling
public class StationInfoSet {
    private final StationInformationSetService stationInformationSetService;


    /*public void setInfo() throws IOException, ParseException, NullPointerException {


        DateTimeFormatter formatt = DateTimeFormatter.ofPattern("yyyyMM");
        LocalDate start = LocalDate.of(2022, 05, 10);
        LocalDate now = LocalDate.now();
        if(start.getYear() == now.getYear() && start.getMonth() == now.getMonth()) {
            return;
        }

        int count = 0;
        boolean flag = true;

        while (flag) {
            count++;
            log.info(start.format(formatt)+" 혼잡도 데이터 저장 중...");
            stationInformationSetService.peopleInformationBySeoulAtTimeSave(start.format(formatt));
            start = start.plusMonths(1);
            if(start.getYear() == now.getYear() && start.getMonth() == now.getMonth()) {
                flag = false;
            }
        }
        log.info("혼잡도 데이터 저장완료");

        log.info("역 정보 데이터 저장중...");
        stationInformationSetService.setStationInformation();
        stationInformationSetService.setLetLon();
        log.info("역 정보 데이터 저장완료");

        stationScehduleSet();

        log.info("혼잡도 평균 저장 중...");
        stationInformationSetService.getStationDegreeOfCongestionAvg(count);
        log.info("혼잡도 평균 저장 완료");
    }

    public void stationScehduleSet() throws IOException {
        List<String> stationList = stationInformationSetService.stationDistinctList();
        log.info("평일 역 시간표 저장 중...");
        for(int i = 0; i < stationList.size(); i++) {
            String json = stationInformationSetService.getStationCode(stationList.get(i));
            stationInformationSetService.stationScheduleCall(json, "1", "1");
            stationInformationSetService.stationScheduleCall(json, "1", "2");
        }
        log.info("평일 역 시간표 저장완료");
        log.info("토요일 역 시간표 저장 중...");
        for(int i = 0; i < stationList.size(); i++) {
            String json = stationInformationSetService.getStationCode(stationList.get(i));
            stationInformationSetService.stationScheduleCall(json, "1", "1");
            stationInformationSetService.stationScheduleCall(json, "1", "2");
        }
        log.info("토요일 역 시간표 저장완료");
        log.info("휴일, 일요일 역 시간표 저장 중...");
        for(int i = 0; i < stationList.size(); i++) {
            String json = stationInformationSetService.getStationCode(stationList.get(i));
            stationInformationSetService.stationScheduleCall(json, "1", "1");
            stationInformationSetService.stationScheduleCall(json, "1", "2");
        }
        log.info("휴일, 일요일 역 시간표 저장완료");
    }

    @Scheduled(fixedDelay = 1000 * 60 * 60 * 24)
    public void dataSlice() throws IOException, ParseException, NullPointerException {
        LocalDate now = LocalDate.now();
        DateTimeFormatter day = DateTimeFormatter.ofPattern("dd");
        DateTimeFormatter monthFormatt = DateTimeFormatter.ofPattern("MM");
        if(now.format(day) != "15") {
            return;
        }
        String month = now.format(monthFormatt);
        stationInformationSetService.peopleInformationBySeoulAtTimeSave("2023" + month);
        *//* 달마다 실행 *//*
        stationInformationSetService.getStationDegreeOfCongestionAvg();
    }*/
}
