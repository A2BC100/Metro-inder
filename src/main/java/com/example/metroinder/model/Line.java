package com.example.metroinder.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lineId;
    private String line;

    @Builder
    public Line(String line) {
        this.line = line;
    }
}
