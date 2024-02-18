package com.example.metroinder.stationSchedule.service;

import com.example.metroinder.dataSet.repository.CapitalareaStationRepository;
import com.example.metroinder.dataSet.repository.LineRepository;
import com.example.metroinder.dataSet.repository.StationScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class StationScheduleService {

    private final StationScheduleRepository stationScheduleRepository;
    private final CapitalareaStationRepository capitalareaStationRepository;
    private final LineRepository lineRepository;

    public Map<String, Object> findStationSchedule(String stationName) {
        Map<String, Object> map = new HashMap<>();
        List<StationScheduleRepository.stationScheduleInfo> weekdayUpSchduleList = stationScheduleRepository.findWeekdayUp(stationName);
        List<StationScheduleRepository.stationScheduleInfo> weekdayDownSchduleList = stationScheduleRepository.findWeekdayDown(stationName);
        List<StationScheduleRepository.stationScheduleInfo> saturdayUpSchduleList = stationScheduleRepository.findSaturdayUp(stationName);
        List<StationScheduleRepository.stationScheduleInfo> saturdayDownSchduleList = stationScheduleRepository.findSaturdayDown(stationName);
        List<StationScheduleRepository.stationScheduleInfo> sundayUpSchduleList = stationScheduleRepository.findSundayUp(stationName);
        List<StationScheduleRepository.stationScheduleInfo> sundayDownSchduleList = stationScheduleRepository.findSundayDown(stationName);

        map.put("weekdayUp", weekdayUpSchduleList);
        map.put("weekdayDown", weekdayDownSchduleList);
        map.put("saturdayUp", saturdayUpSchduleList);
        map.put("saturdayDown", saturdayDownSchduleList);
        map.put("sundayUp", sundayUpSchduleList);
        map.put("sundayDown", sundayDownSchduleList);

        return map;
    }

}
