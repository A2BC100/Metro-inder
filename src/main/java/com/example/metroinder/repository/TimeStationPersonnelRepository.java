package com.example.metroinder.repository;

import com.example.metroinder.model.TimeStationPersonnel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TimeStationPersonnelRepository extends JpaRepository<TimeStationPersonnel, Long> {
    //@Query(value = "select select station, sum(oneRide), sum(twoRide),sum(threeRide), sum(fourRide), sum(fiveRide), sum(sixRide),
    //sum(sevenRide), sum(eightRide), sum(nineRide), sum(tenRide), sum(elevenRide), sum(twelveRide), sum(thirteenRide),
    //sum(fourteenRide), sum(fifteenRide), sum(sixteenRide), sum(seventeenRide), sum(eighteenRide), sum(nineteenRide),
    //sum(twentyRide), sum(twentyoneRide), sum(twentytwoRide), sum(twentythreeRide), sum(midnightRide)  from TimeStationPersonnel group by station");
    //List<TimeStationPersonnel> sameStationList();
}
