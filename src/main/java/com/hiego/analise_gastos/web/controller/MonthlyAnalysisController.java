package com.hiego.analise_gastos.web.controller;

import com.hiego.analise_gastos.core.entity.MonthlyAnalysis;
import com.hiego.analise_gastos.core.service.MonthlyAnalysisService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/analysis")
public class MonthlyAnalysisController {

    private final MonthlyAnalysisService service;

    public MonthlyAnalysisController(MonthlyAnalysisService service) {
        this.service = service;
    }

    @GetMapping("/analysis")
    public ResponseEntity<Optional<MonthlyAnalysis>> getMonthlyAnalysis(@RequestParam String year,
                                                                        @RequestParam String month){
        return ResponseEntity.ok(service.getByMonthAndYear(year, month));
    }

    @GetMapping("/compare")
    public ResponseEntity<String> compareTwoMonths(@RequestParam String year1,
                                                   @RequestParam String month1,
                                                   @RequestParam String year2,
                                                   @RequestParam String month2){
        return ResponseEntity.ok(service.compareWithOtherMonth(year1, month1, year2, month2));
    }
}
