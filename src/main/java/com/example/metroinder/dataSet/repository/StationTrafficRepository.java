package com.example.metroinder.dataSet.repository;

import com.example.metroinder.dataSet.model.StationTraffic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface StationTrafficRepository extends JpaRepository<StationTraffic, Long> {

    /*@Query(value = "SELECT DISTINCT(record_date) FROM station_traffic;", nativeQuery = true)
    String selectRecordDate();*/

    @Query(value = "select station, line, (sum(one_ride) / :months) / 3200  AS oneRide, (sum(two_ride) / :months) / 3200 AS twoRide, (sum(three_ride) / :months) / 3200 AS threeRide, (sum(four_ride) / :months) / 3200 AS fourRide, (sum(five_ride) / :months) / 3200 AS fiveRide, (sum(six_ride) / :months) / 3200 AS sixRide, (sum(seven_ride) / :months) / 3200 AS sevenRide, (sum(eight_ride) / :months) / 3200 AS eightRide, (sum(nine_ride) / :months) / 3200 AS nineRide, (sum(ten_ride) / :months) / 3200 AS tenRide, (sum(eleven_ride) / :months) / 3200 AS elevenRide, (sum(twelve_ride) / :months) / 3200 AS twelveRide, (sum(thirteen_ride) / :months) / 3200 AS thirteenRide, (sum(fourteen_ride) / :months) / 3200 AS fourteenRide, (sum(fifteen_ride) / :months) / 3200 AS fifteenRide, (sum(sixteen_ride) / :months) / 3200 AS sixteenRide, (sum(seventeen_ride) / :months) / 3200 AS seventeenRide, (sum(eighteen_ride) / :months) / 3200 AS eighteenRide, (sum(nineteen_ride) / :months) / 3200 AS nineteenRide, (sum(twenty_ride) / :months) / 3200 AS twentyRide, (sum(twentyone_ride) / :months) / 3200 AS twentyoneRide, (sum(twentytwo_ride) / :months) / 3200 AS twentytwoRide, (sum(twentythree_ride) / :months) / 3200 AS twentythreeRide, (sum(midnight_ride) / :months) / 3200 AS midnightRide from station_traffic where station = :stationName and line = :lineName group by station, line", nativeQuery = true)
    SameStationPeople stationDegreeOfCongestion(@Param("months") int months, @Param("stationName") String stationName, @Param("lineName") String lineName);

    @Query(value = "SELECT line FROM station_traffic WHERE station_number = :stationNumber ORDER BY congestion_id ASC LIMIT 1", nativeQuery = true)
    String findLineDate(@Param("stationNumber") int stationNum);

    @Query(value = "SELECT * FROM station_traffic WHERE STR_TO_DATE(record_date, '%Y-%m-%d') BETWEEN '2015-01-01' AND '2023-10-30' ORDER BY record_date ASC, congestion_id ASC", nativeQuery = true)
    List<StationTraffic> findAllByOrderByRecordDateDesc();

    @Query(value = "SELECT * FROM station_traffic WHERE line = :line and station = :station and record_date = :date", nativeQuery = true)
    StationTraffic findLineAndStationAndRecordDate(@Param("line") String line, @Param("station") String station, @Param("date") String date);

    @Query(value = "SELECT * FROM station_traffic WHERE line = :line and station LIKE :station and record_date = :date", nativeQuery = true)
    StationTraffic findReanamedStation(@Param("line") String line, @Param("station") String station, @Param("date") String date);

    public static interface SameStationPeople {
        String getStation();
        String getLine();
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
