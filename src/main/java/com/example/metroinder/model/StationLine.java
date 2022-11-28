package com.example.metroinder.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;


import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class StationLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stationLineId;

    @ManyToOne
    @JoinColumn(name="station")
    private CapitalareaStation capitalareaStation;

    @ManyToOne
    @JoinColumn(name = "line")
    private Line line;
    private int lineOrder;
    @ColumnDefault("0")
    private int oneRide;
    @ColumnDefault("0")
    private int twoRide;
    @ColumnDefault("0")
    private int threeRide;
    @ColumnDefault("0")
    private int fourRide;
    @ColumnDefault("0")
    private int fiveRide;
    @ColumnDefault("0")
    private int sixRide;
    @ColumnDefault("0")
    private int sevenRide;
    @ColumnDefault("0")
    private int eightRide;
    @ColumnDefault("0")
    private int nineRide;
    @ColumnDefault("0")
    private int tenRide;
    @ColumnDefault("0")
    private int elevenRide;
    @ColumnDefault("0")
    private int twelveRide;
    @ColumnDefault("0")
    private int thirteenRide;
    @ColumnDefault("0")
    private int fourteenRide;
    @ColumnDefault("0")
    private int fifteenRide;
    @ColumnDefault("0")
    private int sixteenRide;
    @ColumnDefault("0")
    private int seventeenRide;
    @ColumnDefault("0")
    private int eighteenRide;
    @ColumnDefault("0")
    private int nineteenRide;
    @ColumnDefault("0")
    private int twentyRide;
    @ColumnDefault("0")
    private int twentyoneRide;
    @ColumnDefault("0")
    private int twentytwoRide;
    @ColumnDefault("0")
    private int twentythreeRide;
    @ColumnDefault("0")
    private int midnightRide;

    @Builder
    public StationLine(CapitalareaStation capitalareaStation, Line line, int lineOrder, int oneRide, int twoRide, int threeRide, int fourRide, int fiveRide, int sixRide, int sevenRide, int eightRide, int nineRide, int tenRide, int elevenRide, int twelveRide, int thirteenRide, int fourteenRide, int fifteenRide, int sixteenRide, int seventeenRide, int eighteenRide, int nineteenRide, int twentyRide, int twentyoneRide, int twentytwoRide, int twentythreeRide, int midnightRide) {
        this.capitalareaStation = capitalareaStation;
        this.line = line;
        this.lineOrder = lineOrder;
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
