package com.example.metroinder.stationSchedule.dto;

import com.example.metroinder.dataSet.model.StationSchedule;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class StationScheduleDto {
    private Long stationId;
    private String line;
    private String station;
    private String arrivalTime;
    private String  departureTime;
    private String arrivalStation;
    private String departureStation;
    private String week;
    private String upDown;
    private String express;

    @Builder
    public StationScheduleDto(Long stationId, String line, String station, String arrivalTime, String departureTime, String arrivalStation, String departureStation, String week, String upDown, String express) {
        this.stationId = stationId;
        this.line = line;
        this.station = station;
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
        this.arrivalStation = arrivalStation;
        this.departureStation = departureStation;
        this.week = week;
        this.upDown = upDown;
        this.express = express;
    }

    public List<StationSchedule> toEntityList(List<StationScheduleDto> stationScheduleDtoList) {
        List<StationSchedule> stationScheduleList = new ArrayList<>();
        for(StationScheduleDto stationScheduleDto : stationScheduleDtoList) {
            StationSchedule stationSchedule = StationSchedule.builder()
                    .station(stationScheduleDto.getStation())
                    .line(stationScheduleDto.getLine())
                    .arrivalTime(stationScheduleDto.getArrivalTime())
                    .departureTime(stationScheduleDto.getDepartureTime())
                    .arrivalStation(stationScheduleDto.getArrivalStation())
                    .departureStation(stationScheduleDto.getDepartureStation())
                    .week(stationScheduleDto.getWeek())
                    .upDown(stationScheduleDto.getUpDown())
                    .express(stationScheduleDto.getExpress())
                    .build();
            stationScheduleList.add(stationSchedule);
        }
        return stationScheduleList;
    }
}