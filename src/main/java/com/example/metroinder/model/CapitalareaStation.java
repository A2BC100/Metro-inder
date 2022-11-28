package com.example.metroinder.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@NoArgsConstructor
@Entity
@Getter
@Setter
public class CapitalareaStation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stationId;
    private String station;
    private Double lat;
    private Double lng;

    @Builder
    public CapitalareaStation(Long stationId, String station, Double lat, Double lng) {
        this.stationId = stationId;
        this.station = station;
        this.lat = lat;
        this.lng = lng;
    }
}
