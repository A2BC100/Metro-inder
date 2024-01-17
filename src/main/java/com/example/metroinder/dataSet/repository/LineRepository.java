package com.example.metroinder.dataSet.repository;

import com.example.metroinder.dataSet.model.Line;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LineRepository extends JpaRepository<Line, Long> {
    Line findByLine(String line);
}
