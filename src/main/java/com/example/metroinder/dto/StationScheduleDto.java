package com.example.metroinder.dto;

import com.example.metroinder.model.StationSchedule;
import com.example.metroinder.model.TimeStationPersonnel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@NoArgsConstructor
public class StationScheduleDto {
    private Long stationId;
    private String station;
    private String arrivalTime;
    private String  departureTime;
    private String arrivalStation;
    private String departureStation;
    private String week;
    private String upDown;
    private String express;

    @Builder
    public StationScheduleDto(Long stationId, String station, String arrivalTime, String departureTime, String arrivalStation, String departureStation, String week, String upDown, String express) {
        this.stationId = stationId;
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
