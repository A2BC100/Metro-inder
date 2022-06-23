package com.example.demo.dto;

import com.example.demo.model.TimeStationPersonnel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class TimeStationPersonnelDto {
    private String station;
    private int oneRide;
    private int twoRide;
    private int threeRide;
    private int fourRide;
    private int fiveRide;
    private int sixRide;
    private int sevenRide;
    private int eightRide;
    private int nineRide;
    private int tenRide;
    private int elevenRide;
    private int twelveRide;
    private int thirteenRide;
    private int fourteenRide;
    private int fifteenRide;
    private int sixteenRide;
    private int seventeenRide;
    private int eighteenRide;
    private int nineteenRide;
    private int twentyRide;
    private int twentyoneRide;
    private int twentytwoRide;
    private int twentythreeRide;
    private int midnightRide;

    public List<TimeStationPersonnel> toEntityList() {

    }
}
