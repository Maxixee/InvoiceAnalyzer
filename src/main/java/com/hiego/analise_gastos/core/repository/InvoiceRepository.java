package com.hiego.analise_gastos.core.repository;

import com.hiego.analise_gastos.core.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    @Query("SELECT i FROM Invoice i WHERE SUBSTRING(i.date, 1, 4) = :year AND SUBSTRING(i.date, 6, 2) = :month")
    List<Invoice> findByYearAndMonth(@Param("year") String year, @Param("month") String month);
}
