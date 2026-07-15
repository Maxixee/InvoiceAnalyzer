package com.hiego.analise_gastos.core.service;

import com.hiego.analise_gastos.core.entity.MonthlyAnalysis;
import com.hiego.analise_gastos.core.repository.MontlyAnalysisRepository;
import com.hiego.analise_gastos.core.service.mapper.MonthlyAnalysisMapper;
import com.hiego.analise_gastos.core.service.utils.CategoryAnalysis;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MonthlyAnalysisService {

    private final MontlyAnalysisRepository repository;
    private final MonthlyAnalysisMapper mapper;

    public MonthlyAnalysisService(MontlyAnalysisRepository repository, MonthlyAnalysisMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public MonthlyAnalysis save(CategoryAnalysis categories){
        MonthlyAnalysis monthly = mapper.responseMonthlyAnalysis(categories);
        return repository.save(monthly);
    }

    public List<MonthlyAnalysis> getByMonthAndYear(String year, String month){
        return repository.findByYearAndMonth(year, month);
    }
}
