package com.example.metroinder.repository;

import com.example.metroinder.model.TimeStationPersonnel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TimeStationPersonnelRepository extends JpaRepository<TimeStationPersonnel, Long> {
    //@Query(value = "select * from TimeStationPersonnel group by station");
    //List<TimeStationPersonnel> sameStationList(String station);
}
