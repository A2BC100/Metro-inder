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
    @ColumnDefault("0")
    private int one;
    @ColumnDefault("0")
    private int two;
    @ColumnDefault("0")
    private int three;
    @ColumnDefault("0")
    private int four;
    @ColumnDefault("0")
    private int five;
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
    private int twentyThree;
    @ColumnDefault("0")
    private int midnight;
    private String recordMonth;
    @Builder
    public TimeStationPersonnel(Long congestionId, String station, String line, int one, int two, int three, int four, int five, int six, int seven, int eight, int nine, int ten, int eleven, int twelve, int thirteen, int fourteen, int fifteen, int sixteen, int seventeen, int eighteen, int nineteen, int twenty, int twentyOne, int twentyTwo, int twentyThree, int midnight, String recordMonth) {
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
}