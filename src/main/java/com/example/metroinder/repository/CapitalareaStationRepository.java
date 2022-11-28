package com.example.metroinder.repository;

import com.example.metroinder.model.CapitalareaStation;
import com.example.metroinder.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CapitalareaStationRepository extends JpaRepository<CapitalareaStation, Long> {
    CapitalareaStation findByStation(String station);

    @Query(value = "select distinct station  from capitalarea_station where station not like '%민주묘지'", nativeQuery = true)
    List<String> findDistinctStation();
}
