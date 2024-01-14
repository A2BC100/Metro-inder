package com.example.metroinder.repository;

import com.example.metroinder.model.StationSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StationScheduleRepository extends JpaRepository<StationSchedule, Long> {
    List<StationSchedule> findByStation(String station);
}
