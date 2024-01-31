package com.example.metroinder.dataSet.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TimeStationPersonnel extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long congestionId;
    private String station;
    private String line;
    private int stationNumber;
    @ColumnDefault("0")
    private int six;
    @ColumnDefault("0")
    private int seven;
    @ColumnDefault("0")
    private int eight;
    @ColumnDefault("0")
    private int nine;
    @ColumnDefault("0")
    private int ten;
    @ColumnDefault("0")
    private int eleven;
    @ColumnDefault("0")
    private int twelve;
    @ColumnDefault("0")
    private int thirteen;
    @ColumnDefault("0")
    private int fourteen;
    @ColumnDefault("0")
    private int fifteen;
    @ColumnDefault("0")
    private int sixteen;
    @ColumnDefault("0")
    private int seventeen;
    @ColumnDefault("0")
    private int eighteen;
    @ColumnDefault("0")
    private int nineteen;
    @ColumnDefault("0")
    private int twenty;
    @ColumnDefault("0")
    private int twentyOne;
    @ColumnDefault("0")
    private int twentyTwo;
    @ColumnDefault("0")
    private int fromTwentyThreeToSixHour;
    private String recordDate;
    @Builder
    public TimeStationPersonnel(Long congestionId, String station, String line, int stationNumber, int six, int seven, int eight, int nine, int ten, int eleven, int twelve, int thirteen, int fourteen, int fifteen, int sixteen, int seventeen, int eighteen, int nineteen, int twenty, int twentyOne, int twentyTwo, int fromTwentyThreeToSixHour, String recordDate) {
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
        this.twentyOne = twentyOne;
        this.twentyTwo = twentyTwo;
        this.fromTwentyThreeToSixHour = fromTwentyThreeToSixHour;
        this.recordDate = recordDate;
    }
}