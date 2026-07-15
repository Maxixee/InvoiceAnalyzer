package com.hiego.analise_gastos.core.repository;

import com.hiego.analise_gastos.core.entity.MonthlyAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MontlyAnalysisRepository extends JpaRepository<MonthlyAnalysis, Long> {

    @Query("SELECT i FROM MonthlyAnalysis i WHERE SUBSTRING(i.date, 1, 4) = :year AND SUBSTRING(i.date, 6, 2) = :month")
    List<MonthlyAnalysis> findByYearAndMonth(@Param("year") String year, @Param("month") String month);
}
