package com.hiego.analise_gastos.core.service.mapper;

import com.hiego.analise_gastos.core.entity.MonthlyAnalysis;
import com.hiego.analise_gastos.core.service.utils.CategoryAnalysis;
import org.springframework.stereotype.Component;

@Component
public class MonthlyAnalysisMapper {

    public MonthlyAnalysis responseMonthlyAnalysis(CategoryAnalysis categories){
        if(categories == null){
            return null;
        }

        MonthlyAnalysis analysis = new MonthlyAnalysis();
        analysis.setDate(categories.getData());
        analysis.setAlimentacao(categories.getAlimentacao().getValor());
        analysis.setEntretenimento(categories.getEntretenimento().getValor());
        analysis.setEstudos(categories.getEstudos().getValor());
        analysis.setSaude(categories.getSaude().getValor());
        analysis.setTransporte(categories.getTransporte().getValor());
        analysis.setOutros(categories.getOutros().getValor());

        Double total = analysis.getAlimentacao()
                        + analysis.getEntretenimento()
                        + analysis.getEstudos()
                        + analysis.getSaude()
                        + analysis.getTransporte()
                        + analysis.getOutros();

        analysis.setTotal(total);

        return analysis;
    }
}
