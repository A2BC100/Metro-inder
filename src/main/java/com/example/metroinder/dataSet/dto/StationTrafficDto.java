package com.example.metroinder.dataSet.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StationTrafficDto {
    private Long congestionId;
    private String station;
    private String line;
    private int stationNumber;
    private int six;
    private int seven;
    private int eight;
    private int nine;
    private int ten;
    private int eleven;
    private int twelve;
    private int thirteen;
    private int fourteen;
    private int fifteen;
    private int sixteen;
    private int seventeen;
    private int eighteen;
    private int nineteen;
    private int twenty;
    private int twentyone;
    private int twentytwo;
    private int fromTwentythreeToSixHour;
    private String recordDate;

    @Builder
    public StationTrafficDto(Long congestionId, String station, String line, int stationNumber, int six, int seven, int eight, int nine, int ten, int eleven, int twelve, int thirteen, int fourteen, int fifteen, int sixteen, int seventeen, int eighteen, int nineteen, int twenty, int twentyone, int twentytwo, int fromTwentythreeToSixHour, String recordDate) {
        this.congestionId = congestionId;
        this.station = station;
        this.line = line;
        this.stationNumber = stationNumber;
        this.six = six;
        this.seven = seven;
        this.eight = eight;
        this.nine = nine;
        this.ten = ten;
        this.eleven = eleven;
        this.twelve = twelve;
        this.thirteen = thirteen;
        this.fourteen = fourteen;
        this.fifteen = fifteen;
        this.sixteen = sixteen;
        this.seventeen = seventeen;
        this.eighteen = eighteen;
        this.nineteen = nineteen;
        this.twenty = twenty;
        this.twentyone = twentyone;
        this.twentytwo = twentytwo;
        this.fromTwentythreeToSixHour = fromTwentythreeToSixHour;
        this.recordDate = recordDate;
    }
}