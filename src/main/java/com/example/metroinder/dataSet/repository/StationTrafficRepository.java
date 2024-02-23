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


    // 모델 학습 데이터
    @Query(value = "SELECT * FROM station_traffic WHERE STR_TO_DATE(record_date, '%Y-%m-%d') BETWEEN '2015-01-01' AND '2021-03-19' ORDER BY record_date ASC, congestion_id ASC", nativeQuery = true)
    List<StationTraffic> getTrainningData();

    // 모델 테스트 데이터
    @Query(value = "SELECT * FROM station_traffic WHERE STR_TO_DATE(record_date, '%Y-%m-%d') BETWEEN '2021-03-20' AND '2022-12-14' ORDER BY record_date ASC, congestion_id ASC", nativeQuery = true)
    List<StationTraffic> getTestingData();

    // 모델 검증 데이터
    @Query(value = "SELECT * FROM station_traffic WHERE STR_TO_DATE(record_date, '%Y-%m-%d') BETWEEN '2022-12-15' AND '2023-10-30' ORDER BY record_date ASC, congestion_id ASC", nativeQuery = true)
    List<StationTraffic> getValidatingData();

    // 추후 학습용 전체 데이터 가져오기
    @Query(value = "SELECT * FROM station_traffic WHERE STR_TO_DATE(record_date, '%Y-%m-%d') BETWEEN '2015-01-01' AND '2023-10-30' ORDER BY record_date ASC, congestion_id ASC", nativeQuery = true)
    List<StationTraffic> findAllByOrderByRecordDateDesc();

    @Query(value = "SELECT * FROM station_traffic WHERE station_number = :stationNumber and record_date = :date", nativeQuery = true)
    StationTraffic findStationAndRecordDate(@Param("stationNumber") int stationNumber, @Param("date") String date);

    @Query(value = "SELECT station_number FROM station_traffic WHERE line = :line and station = :station LIMIT 1", nativeQuery = true)
    int findStationNumber(@Param("station") String station, @Param("line") String line);

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
