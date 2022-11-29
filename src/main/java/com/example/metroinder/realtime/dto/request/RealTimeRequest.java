package com.example.metroinder.realtime.dto.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RealTimeRequest {
    public class RealTimeStationRequest{
        public String station;
    }
    public class RealTimeWeatherResquest{
        private double lat;

        private double lon;

    }

}
