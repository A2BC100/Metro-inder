package com.example.metroinder.dto;

import com.example.metroinder.model.TimeStationPersonnel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class TimeStationPersonnelDto {
    private Long congestionId;
    private String station;
    private String line;
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
    private String recordMonth;

    @Builder
    public TimeStationPersonnelDto(Long congestionId, String station, String line, int oneRide, int twoRide, int threeRide, int fourRide, int fiveRide, int sixRide, int sevenRide, int eightRide, int nineRide, int tenRide, int elevenRide, int twelveRide, int thirteenRide, int fourteenRide, int fifteenRide, int sixteenRide, int seventeenRide, int eighteenRide, int nineteenRide, int twentyRide, int twentyoneRide, int twentytwoRide, int twentythreeRide, int midnightRide, String recordMonth) {
        this.congestionId = congestionId;
        this.station = station;
        this.line = line;
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
        this.recordMonth = recordMonth;
    }

    public List<TimeStationPersonnel> toEntityList(List<TimeStationPersonnelDto> jsonSameStationDtoList) {
        List<TimeStationPersonnel> jsonSameStationList = new ArrayList<>();
        for(TimeStationPersonnelDto timeStationPersonnelDto : jsonSameStationDtoList) {
            TimeStationPersonnel timeStationPersonnel = TimeStationPersonnel.builder()
                    .station(timeStationPersonnelDto.getStation())
                    .line(timeStationPersonnelDto.getLine())
                    .oneRide(timeStationPersonnelDto.getOneRide())
                    .twoRide(timeStationPersonnelDto.getTwoRide())
                    .threeRide(timeStationPersonnelDto.getThreeRide())
                    .fourRide(timeStationPersonnelDto.getFourRide())
                    .fiveRide(timeStationPersonnelDto.getFiveRide())
                    .sixRide(timeStationPersonnelDto.getSixRide())
                    .sevenRide(timeStationPersonnelDto.getSevenRide())
                    .eightRide(timeStationPersonnelDto.getEightRide())
                    .nineRide(timeStationPersonnelDto.getNineRide())
                    .tenRide(timeStationPersonnelDto.getTenRide())
                    .elevenRide(timeStationPersonnelDto.getElevenRide())
                    .twelveRide(timeStationPersonnelDto.getTwelveRide())
                    .thirteenRide(timeStationPersonnelDto.getThirteenRide())
                    .fourteenRide(timeStationPersonnelDto.getFourteenRide())
                    .fifteenRide(timeStationPersonnelDto.getFifteenRide())
                    .sixteenRide(timeStationPersonnelDto.getSixteenRide())
                    .seventeenRide(timeStationPersonnelDto.getSeventeenRide())
                    .eighteenRide(timeStationPersonnelDto.getEighteenRide())
                    .nineteenRide(timeStationPersonnelDto.getNineteenRide())
                    .twentyRide(timeStationPersonnelDto.getTwentyRide())
                    .twentyoneRide(timeStationPersonnelDto.getTwentyoneRide())
                    .twentytwoRide(timeStationPersonnelDto.getTwentytwoRide())
                    .twentythreeRide(timeStationPersonnelDto.getTwentythreeRide())
                    .midnightRide(timeStationPersonnelDto.getMidnightRide())
                    .recordMonth(timeStationPersonnelDto.getRecordMonth())
                    .build();
            jsonSameStationList.add(timeStationPersonnel);
        }
        return jsonSameStationList;
    }
}
