package com.example.metroinder.repository;

import com.example.metroinder.dto.TimeStationPersonnelDto;
import com.example.metroinder.model.TimeStationPersonnel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface TimeStationPersonnelRepository extends JpaRepository<TimeStationPersonnel, Long> {
    @Query(value = "select station, sum(one_ride) AS oneRide, sum(two_ride) AS twoRide, sum(three_ride) AS threeRide, sum(four_ride) AS fourRide, sum(five_ride) AS fiveRide, sum(six_ride) AS sixRide, sum(seven_ride) AS sevenRide, sum(eight_ride) AS eightRide, sum(nine_ride) AS nineRide, sum(ten_ride) AS tenRide, sum(eleven_ride) AS elevenRide, sum(twelve_ride) AS twelveRide, sum(thirteen_ride) AS thirteenRide, sum(fourteen_ride) AS fourteenRide, sum(fifteen_ride) AS fifteenRide, sum(sixteen_ride) AS sixteenRide, sum(seventeen_ride) AS seventeenRide, sum(eighteen_ride) AS eighteenRide, sum(nineteen_ride) AS nineteenRide, sum(twenty_ride) AS twentyRide, sum(twentyone_ride) AS twentyoneRide, sum(twentytwo_ride) AS twentytwoRide, sum(twentythree_ride) AS twentythreeRide, sum(midnight_ride) AS midnightRide from time_station_personnel group by station", nativeQuery = true)
    List<SameStationPeople> findSameStationPeople();

    public static interface SameStationPeople {
        String getStation();
        Long getOneRide();
        Long getTwoRide();
        Long getThreeRide();
        Long getFourRide();
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
