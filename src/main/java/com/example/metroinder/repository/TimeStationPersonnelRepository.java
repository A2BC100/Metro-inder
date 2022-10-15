package com.example.metroinder.repository;

import com.example.metroinder.dto.TimeStationPersonnelDto;
import com.example.metroinder.model.TimeStationPersonnel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface TimeStationPersonnelRepository extends JpaRepository<TimeStationPersonnel, Long> {
    @Query(value = "select station, sum(one_ride) / :months  AS oneRide, sum(two_ride) / :months AS twoRide, sum(three_ride) / :months AS threeRide, sum(four_ride) / :months AS fourRide, sum(five_ride) / :months AS fiveRide, sum(six_ride) / :months AS sixRide, sum(seven_ride) / :months AS sevenRide, sum(eight_ride) / :months AS eightRide, sum(nine_ride) / :months AS nineRide, sum(ten_ride) / :months AS tenRide, sum(eleven_ride) / :months AS elevenRide, sum(twelve_ride) / :months AS twelveRide, sum(thirteen_ride) / :months AS thirteenRide, sum(fourteen_ride) / :months AS fourteenRide, sum(fifteen_ride) / :months AS fifteenRide, sum(sixteen_ride) / :months AS sixteenRide, sum(seventeen_ride) / :months AS seventeenRide, sum(eighteen_ride) / :months AS eighteenRide, sum(nineteen_ride) / :months AS nineteenRide, sum(twenty_ride) / :months AS twentyRide, sum(twentyone_ride) / :months AS twentyoneRide, sum(twentytwo_ride) / :months AS twentytwoRide, sum(twentythree_ride) / :months AS twentythreeRide, sum(midnight_ride) / :months AS midnightRide from time_station_personnel group by station", nativeQuery = true)
    List<SameStationPeople> findSameStationPeople(@Param("months") int months);

    @Query(value = "select station, (sum(one_ride) / :months) / 3400  AS oneRide, (sum(two_ride) / :months) / 3400 AS twoRide, (sum(three_ride) / :months) / 3400 AS threeRide, (sum(four_ride) / :months) / 3400 AS fourRide, (sum(five_ride) / :months) / 3400 AS fiveRide, (sum(six_ride) / :months) / 3400 AS sixRide, (sum(seven_ride) / :months) / 3400 AS sevenRide, (sum(eight_ride) / :months) / 3400 AS eightRide, (sum(nine_ride) / :months) / 3400 AS nineRide, (sum(ten_ride) / :months) / 3400 AS tenRide, (sum(eleven_ride) / :months) / 3400 AS elevenRide, (sum(twelve_ride) / :months) / 3400 AS twelveRide, (sum(thirteen_ride) / :months) / 3400 AS thirteenRide, (sum(fourteen_ride) / :months) / 3400 AS fourteenRide, (sum(fifteen_ride) / :months) / 3400 AS fifteenRide, (sum(sixteen_ride) / :months) / 3400 AS sixteenRide, (sum(seventeen_ride) / :months) / 3400 AS seventeenRide, (sum(eighteen_ride) / :months) / 3400 AS eighteenRide, (sum(nineteen_ride) / :months) / 3400 AS nineteenRide, (sum(twenty_ride) / :months) / 3400 AS twentyRide, (sum(twentyone_ride) / :months) / 3400 AS twentyoneRide, (sum(twentytwo_ride) / :months) / 3400 AS twentytwoRide, (sum(twentythree_ride) / :months) / 3400 AS twentythreeRide, (sum(midnight_ride) / :months) / 3400 AS midnightRide from time_station_personnel group by station", nativeQuery = true)
    List<SameStationPeople> stationDegreeOfCongestion(@Param("months") int months);

    @Modifying
    @Transactional
    @Query(value = "truncate time_station_personnel", nativeQuery = true)
    void resetTimeStationPersonnel();

    TimeStationPersonnel findByStation(String station);

    @Query(value = "select distinct station  from time_station_personnel where station not like '%민주묘지'", nativeQuery = true)
    List<String> findDistinctStation();

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
