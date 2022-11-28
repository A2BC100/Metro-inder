package com.example.metroinder.dto;

import com.example.metroinder.model.CapitalareaStation;
import com.example.metroinder.model.StationLine;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
public class CapitalareaStationDto {
    private Long stationId;
    private String station;
    private Double lat;
    private Double lng;
    private List<StationLine> stationLines;

    @Builder
    public CapitalareaStationDto(String station, Double lat, Double lng, List<StationLine> stationLines) {
        this.station = station;
        this.lat = lat;
        this.lng = lng;
        this.stationLines = stationLines;
    }

    public CapitalareaStation toEntity(CapitalareaStationDto capitalareaStationDto) {
        CapitalareaStation capitalareaStation = CapitalareaStation.builder()
                .station(capitalareaStationDto.station)
                .lat(capitalareaStationDto.lat)
                .lng(capitalareaStationDto.lng)
                .build();
        return capitalareaStation;
    }
    public List<CapitalareaStation> toEntityList(List<CapitalareaStationDto> capitalareaStationDtoList) {
        List<CapitalareaStation> capitalareaStationList = new ArrayList<>();
        for(CapitalareaStationDto capitalareaStationDto : capitalareaStationDtoList) {
            CapitalareaStation capitalareaStation = CapitalareaStation.builder()
                    .station(capitalareaStationDto.station)
                    .lat(capitalareaStationDto.lat)
                    .lng(capitalareaStationDto.lng)
                    .build();
            capitalareaStationList.add(capitalareaStation);
        }
        return capitalareaStationList;
    }
}
