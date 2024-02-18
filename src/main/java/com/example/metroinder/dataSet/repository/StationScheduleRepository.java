package com.example.metroinder.dataSet.repository;

import com.example.metroinder.dataSet.model.StationSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface StationScheduleRepository extends JpaRepository<StationSchedule, Long> {
    List<StationSchedule> findByStation(String station);

    // 평일 상행 시간표
    @Query(value = "select arrival_station, arrival_time, departure_station, departure_time, express from station_schedule where station = :stationName and week ='평일' and up_down = '상행, 내선' ", nativeQuery = true)
    List<stationScheduleInfo> findWeekdayUp(@Param("stationName") String stationName);
    // 평일 하행 시간표
    @Query(value = "select arrival_station, arrival_time, departure_station, departure_time, express from station_schedule where station = :stationName and week ='평일' and up_down = '하행, 외선' ", nativeQuery = true)
    List<stationScheduleInfo> findWeekdayDown(@Param("stationName") String stationName);
    // 토요일 상행 시간표
    @Query(value = "select arrival_station, arrival_time, departure_station, departure_time, express from station_schedule where station = :stationName and week ='토요일' and up_down = '상행, 내선' ", nativeQuery = true)
    List<stationScheduleInfo> findSaturdayUp(@Param("stationName") String stationName);
    // 토요일 하행 시간표
    @Query(value = "select arrival_station, arrival_time, departure_station, departure_time, express from station_schedule where station = :stationName and week ='토요일' and up_down = '하행, 외선' ", nativeQuery = true)
    List<stationScheduleInfo> findSaturdayDown(@Param("stationName") String stationName);
    // 휴일, 일요일 상행 시간표
    @Query(value = "select arrival_station, arrival_time, departure_station, departure_time, express from station_schedule where station = :stationName and week ='휴일/일요일' and up_down = '상행, 내선' ", nativeQuery = true)
    List<stationScheduleInfo> findSundayUp(@Param("stationName") String stationName);
    // 휴일, 일요일 하행 시간표
    @Query(value = "select arrival_station, arrival_time, departure_station, departure_time, express from station_schedule where station = :stationName and week ='휴일/일요일' and up_down = '하행, 외선' ", nativeQuery = true)
    List<stationScheduleInfo> findSundayDown(@Param("stationName") String stationName);


    public static interface stationScheduleInfo {
        String getArrival_station();
        String getArrival_time();
        String getDeparture_station();
        String getDeparture_time();
        String getExpress();
    }
}
