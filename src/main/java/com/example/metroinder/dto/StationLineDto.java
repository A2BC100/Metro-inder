package com.example.metroinder.dto;

import com.example.metroinder.model.CapitalareaStation;
import com.example.metroinder.model.Line;
import com.example.metroinder.model.StationLine;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class StationLineDto {
    private Long stationLineId;
    private CapitalareaStation capitalareaStation;
    private Line line;
    private int lineOrder;
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
    public StationLineDto(CapitalareaStation capitalareaStation, Line line, int lineOrder, int oneRide, int twoRide, int threeRide, int fourRide, int fiveRide, int sixRide, int sevenRide, int eightRide, int nineRide, int tenRide, int elevenRide, int twelveRide, int thirteenRide, int fourteenRide, int fifteenRide, int sixteenRide, int seventeenRide, int eighteenRide, int nineteenRide, int twentyRide, int twentyoneRide, int twentytwoRide, int twentythreeRide, int midnightRide) {
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

    public StationLine toEntity(StationLineDto stationLineDto){
        StationLine stationLine = StationLine.builder()
                .capitalareaStation(stationLineDto.capitalareaStation)
                .line(stationLineDto.line)
                .lineOrder(stationLineDto.lineOrder)
                .oneRide(stationLineDto.oneRide)
                .twoRide(stationLineDto.twoRide)
                .threeRide(stationLineDto.threeRide)
                .fourRide(stationLineDto.fourRide)
                .fiveRide(stationLineDto.fiveRide)
                .sixRide(stationLineDto.sixRide)
                .sevenRide(stationLineDto.sevenRide)
                .eightRide(stationLineDto.eightRide)
                .nineRide(stationLineDto.nineRide)
                .tenRide(stationLineDto.tenRide)
                .elevenRide(stationLineDto.elevenRide)
                .twelveRide(stationLineDto.twelveRide)
                .thirteenRide(stationLineDto.thirteenRide)
                .fourteenRide(stationLineDto.fourteenRide)
                .fifteenRide(stationLineDto.fifteenRide)
                .sixteenRide(stationLineDto.sixteenRide)
                .seventeenRide(stationLineDto.seventeenRide)
                .eighteenRide(stationLineDto.eighteenRide)
                .nineteenRide(stationLineDto.nineteenRide)
                .twentyRide(stationLineDto.twentyRide)
                .twentyoneRide(stationLineDto.twentyoneRide)
                .twentytwoRide(stationLineDto.twentytwoRide)
                .twentythreeRide(stationLineDto.twentythreeRide)
                .midnightRide(stationLineDto.midnightRide)
                .build();
        return stationLine;
    }
}
