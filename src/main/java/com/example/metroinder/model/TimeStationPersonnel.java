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
public class TimeStationPersonnel extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Builder
    public TimeStationPersonnel(String station, int oneRide, int twoRide, int threeRide, int fourRide, int fiveRide, int sixRide, int sevenRide, int eightRide, int nineRide, int tenRide, int elevenRide, int twelveRide, int thirteenRide, int fourteenRide, int fifteenRide, int sixteenRide, int seventeenRide, int eighteenRide, int nineteenRide, int twentyRide, int twentyoneRide, int twentytwoRide, int twentythreeRide, int midnightRide) {
        this.station = station;
        this.oneRide = oneRide;
        this.twoRide = twoRide;
        this.threeRide = threeRide;
        this.fourRide = fourRide;
        this.fiveRide = fiveRide;
        this.sixRide = sixRide;
        this.sevenRide = sevenRide;
        this.eightRide = eightRide;
        this.nineRide = nineRide;
        this.tenRide = tenRide;
        this.elevenRide = elevenRide;
        this.twelveRide = twelveRide;
        this.thirteenRide = thirteenRide;
        this.fourteenRide = fourteenRide;
        this.fifteenRide = fifteenRide;
        this.sixteenRide = sixteenRide;
        this.seventeenRide = seventeenRide;
        this.eighteenRide = eighteenRide;
        this.nineteenRide = nineteenRide;
        this.twentyRide = twentyRide;
        this.twentyoneRide = twentyoneRide;
        this.twentytwoRide = twentytwoRide;
        this.twentythreeRide = twentythreeRide;
        this.midnightRide = midnightRide;
    }
}
