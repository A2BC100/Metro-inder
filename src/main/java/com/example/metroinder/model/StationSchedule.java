package com.example.metroinder.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Builder
public class StationSchedule extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long stationId;
    private String line;
    private String station;
    private String arrivalTime;
    private String departureTime;
    private String arrivalStation;
    private String departureStation;
    private String week;
    private String upDown;
    private String express;

    @Builder
    public StationSchedule(Long stationId, String line, String station, String arrivalTime, String departureTime, String arrivalStation, String departureStation, String week, String upDown, String express) {
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
}
