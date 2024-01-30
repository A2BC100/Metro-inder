package com.example.metroinder.dataSet.dto;


import com.example.metroinder.dataSet.model.TimeStationPersonnel;
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
    private int one;
    private int two;
    private int three;
    private int four;
    private int five;
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
    private int twentyOne;
    private int twentyTwo;
    private int twentyThree;
    private int midnight;
    private String recordMonth;

    @Builder
    public TimeStationPersonnelDto(Long congestionId, String station, String line, int one, int two, int three, int four, int five, int six, int seven, int eight, int nine, int ten, int eleven, int twelve, int thirteen, int fourteen, int fifteen, int sixteen, int seventeen, int eighteen, int nineteen, int twenty, int twentyOne, int twentyTwo, int twentyThree, int midnight, String recordMonth) {
        this.congestionId = congestionId;
        this.station = station;
        this.line = line;
        this.one = one;
        this.two = two;
        this.three = three;
        this.four = four;
        this.five = five;
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
        this.twentyThree = twentyThree;
        this.midnight = midnight;
        this.recordMonth = recordMonth;
    }

    public List<TimeStationPersonnel> toEntityList(List<TimeStationPersonnelDto> jsonSameStationDtoList) {
        List<TimeStationPersonnel> jsonSameStationList = new ArrayList<>();
        for(TimeStationPersonnelDto timeStationPersonnelDto : jsonSameStationDtoList) {
            TimeStationPersonnel timeStationPersonnel = TimeStationPersonnel.builder()
                    .station(timeStationPersonnelDto.getStation())
                    .line(timeStationPersonnelDto.getLine())
                    .one(timeStationPersonnelDto.getOne())
                    .two(timeStationPersonnelDto.getTwo())
                    .three(timeStationPersonnelDto.getThree())
                    .four(timeStationPersonnelDto.getFour())
                    .five(timeStationPersonnelDto.getFive())
                    .six(timeStationPersonnelDto.getSix())
                    .seven(timeStationPersonnelDto.getSeven())
                    .eight(timeStationPersonnelDto.getEight())
                    .nine(timeStationPersonnelDto.getNine())
                    .ten(timeStationPersonnelDto.getTen())
                    .eleven(timeStationPersonnelDto.getEleven())
                    .twelve(timeStationPersonnelDto.getTwelve())
                    .thirteen(timeStationPersonnelDto.getThirteen())
                    .fourteen(timeStationPersonnelDto.getFourteen())
                    .fifteen(timeStationPersonnelDto.getFifteen())
                    .sixteen(timeStationPersonnelDto.getSixteen())
                    .seventeen(timeStationPersonnelDto.getSeventeen())
                    .eighteen(timeStationPersonnelDto.getEighteen())
                    .nineteen(timeStationPersonnelDto.getNineteen())
                    .twenty(timeStationPersonnelDto.getTwenty())
                    .twentyOne(timeStationPersonnelDto.getTwentyOne())
                    .twentyTwo(timeStationPersonnelDto.getTwentyTwo())
                    .twentyThree(timeStationPersonnelDto.getTwentyThree())
                    .midnight(timeStationPersonnelDto.getMidnight())
                    .recordMonth(timeStationPersonnelDto.getRecordMonth())
                    .build();
            jsonSameStationList.add(timeStationPersonnel);
        }
        return jsonSameStationList;
    }
}