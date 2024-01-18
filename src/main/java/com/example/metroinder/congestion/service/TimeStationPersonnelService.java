package com.example.metroinder.congestion.service;

import com.example.metroinder.dataSet.model.CapitalareaStation;
import com.example.metroinder.dataSet.repository.CapitalareaStationRepository;
import com.example.metroinder.dataSet.repository.StationLineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class TimeStationPersonnelService {

    private final CapitalareaStationRepository capitalareaStationRepository;
    private final StationLineRepository stationLineRepository;

    // 혼잡도 리턴을 위한 메소드
    public Map<String, Object> findSameStationPeople(String data) {
        Map<String, Object> json = new HashMap<String, Object>();

        CapitalareaStation station = capitalareaStationRepository.findByStation(data);
        StationLineRepository.SameStationPeople stationLine = stationLineRepository.stationCongestion(station.getStationId());
        json.put("station", data);
        json.put("1", Long.valueOf(stationLine.getOneRide()).intValue());
        json.put("2", Long.valueOf(stationLine.getTwoRide()).intValue());
        json.put("3", Long.valueOf(stationLine.getThreeRide()).intValue());
        json.put("4", Long.valueOf(stationLine.getFourRide()).intValue());
        json.put("5", Long.valueOf(stationLine.getFiveRide()).intValue());
        json.put("6", Long.valueOf(stationLine.getSixRide()).intValue());
        json.put("7", Long.valueOf(stationLine.getSevenRide()).intValue());
        json.put("8", Long.valueOf(stationLine.getEightRide()).intValue());
        json.put("9", Long.valueOf(stationLine.getNineRide()).intValue());
        json.put("10", Long.valueOf(stationLine.getTenRide()).intValue());
        json.put("11", Long.valueOf(stationLine.getElevenRide()).intValue());
        json.put("12", Long.valueOf(stationLine.getTwelveRide()).intValue());
        json.put("13", Long.valueOf(stationLine.getThirteenRide()).intValue());
        json.put("14", Long.valueOf(stationLine.getFourteenRide()).intValue());
        json.put("15", Long.valueOf(stationLine.getFifteenRide()).intValue());
        json.put("16", Long.valueOf(stationLine.getSixteenRide()).intValue());
        json.put("17", Long.valueOf(stationLine.getSeventeenRide()).intValue());
        json.put("18", Long.valueOf(stationLine.getEighteenRide()).intValue());
        json.put("19", Long.valueOf(stationLine.getNineteenRide()).intValue());
        json.put("20", Long.valueOf(stationLine.getTwentyRide()).intValue());
        json.put("21", Long.valueOf(stationLine.getTwentyoneRide()).intValue());
        json.put("22", Long.valueOf(stationLine.getTwentytwoRide()).intValue());
        json.put("23", Long.valueOf(stationLine.getTwentythreeRide()).intValue());
        json.put("24", Long.valueOf(stationLine.getMidnightRide()).intValue());

        return json;
    }


}