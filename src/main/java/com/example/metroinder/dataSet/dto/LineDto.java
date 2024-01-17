package com.example.metroinder.dataSet.dto;

import com.example.metroinder.dataSet.model.Line;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class LineDto {
    private Long lineId;
    private String line;

    @Builder
    public LineDto(String line) {
        this.line = line;
    }

    public Line toEntity(LineDto lineDto){
        Line line = Line.builder().
                line(lineDto.getLine()).
                build();
        return line;
    }
}

