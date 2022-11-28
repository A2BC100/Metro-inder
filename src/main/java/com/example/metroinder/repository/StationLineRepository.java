package com.example.metroinder.repository;

import com.example.metroinder.model.CapitalareaStation;
import com.example.metroinder.model.Line;
import com.example.metroinder.model.StationLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StationLineRepository  extends JpaRepository<StationLine, Long> {
    List<StationLine> findByCapitalareaStation(CapitalareaStation capitalareaStation);
    StationLine findByLineAndLineOrder(Line line,int LineOrder);

    @Query(value = "select station, sum(one_ride) / count(*)  AS oneRide, sum(two_ride) / count(*) AS twoRide, sum(three_ride) / count(*) AS threeRide, sum(four_ride) / count(*) AS fourRide, sum(five_ride) / count(*) AS fiveRide, sum(six_ride) / count(*) AS sixRide, sum(seven_ride) / count(*) AS sevenRide, sum(eight_ride) / count(*) AS eightRide, sum(nine_ride) / count(*) AS nineRide, sum(ten_ride) / count(*) AS tenRide, sum(eleven_ride) / count(*) AS elevenRide, sum(twelve_ride) / count(*) AS twelveRide, sum(thirteen_ride) / count(*) AS thirteenRide, sum(fourteen_ride) / count(*) AS fourteenRide, sum(fifteen_ride) / count(*) AS fifteenRide, sum(sixteen_ride) / count(*) AS sixteenRide, sum(seventeen_ride) / count(*) AS seventeenRide, sum(eighteen_ride) / count(*) AS eighteenRide, sum(nineteen_ride) / count(*) AS nineteenRide, sum(twenty_ride) / count(*) AS twentyRide, sum(twentyone_ride) / count(*) AS twentyoneRide, sum(twentytwo_ride) / count(*) AS twentytwoRide, sum(twentythree_ride) / count(*) AS twentythreeRide, sum(midnight_ride) / count(*) AS midnightRide from time_station_personnel where station = :stationName group by station", nativeQuery = true)
    SameStationPeople stationCongestion(@Param("stationName") String stationName);

    public static interface SameStationPeople {
        String getStation();
        Integer getOneRide();
        Integer getTwoRide();
        Integer getThreeRide();
        Integer getFourRide();
        Integer getFiveRide();
        Integer getSixRide();
        Integer getSevenRide();
        Integer getEightRide();
        Integer getNineRide();
        Integer getTenRide();
        Integer getElevenRide();
        Integer getTwelveRide();
        Integer getThirteenRide();
        Integer getFourteenRide();
        Integer getFifteenRide();
        Integer getSixteenRide();
        Integer getSeventeenRide();
        Integer getEighteenRide();
        Integer getNineteenRide();
        Integer getTwentyRide();
        Integer getTwentyoneRide();
        Integer getTwentytwoRide();
        Integer getTwentythreeRide();
        Integer getMidnightRide();
    }
}
