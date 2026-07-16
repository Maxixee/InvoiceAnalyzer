package com.hiego.analise_gastos.core.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hiego.analise_gastos.core.entity.MonthlyAnalysis;
import com.hiego.analise_gastos.core.repository.MontlyAnalysisRepository;
import com.hiego.analise_gastos.core.service.mapper.MonthlyAnalysisMapper;
import com.hiego.analise_gastos.core.service.utils.CategoryAnalysis;
import jakarta.persistence.Column;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MonthlyAnalysisService {

    private final MontlyAnalysisRepository repository;
    private final MonthlyAnalysisMapper mapper;
    private final GPTService gpt;
    private final ObjectMapper objectMapper;

    public MonthlyAnalysisService(MontlyAnalysisRepository repository, MonthlyAnalysisMapper mapper, GPTService gpt, ObjectMapper objectMapper) {
        this.repository = repository;
        this.mapper = mapper;
        this.gpt = gpt;
        this.objectMapper = objectMapper;
    }

    public MonthlyAnalysis save(CategoryAnalysis categories){
        MonthlyAnalysis monthly = mapper.responseMonthlyAnalysis(categories);
        return repository.save(monthly);
    }

    public Optional<MonthlyAnalysis> getByMonthAndYear(String year, String month){
        return repository.findByYearAndMonth(year, month);
    }

    public List<MonthlyAnalysis> getAll(){
        return repository.findAll();
    }

    public MonthlyAnalysis getAllStats() {

        MonthlyAnalysis summary = new MonthlyAnalysis();
        summary.setDate("Resumo geral");
        summary.setEntretenimento(0.0);
        summary.setAlimentacao(0.0);
        summary.setEstudos(0.0);
        summary.setSaude(0.0);
        summary.setTransporte(0.0);
        summary.setOutros(0.0);
        summary.setTotal(0.0);

        for (MonthlyAnalysis month : getAll()) {
            summary.setEntretenimento(summary.getEntretenimento() + month.getEntretenimento());
            summary.setAlimentacao(summary.getAlimentacao() + month.getAlimentacao());
            summary.setEstudos(summary.getEstudos() + month.getEstudos());
            summary.setSaude(summary.getSaude() + month.getSaude());
            summary.setTransporte(summary.getTransporte() + month.getTransporte());
            summary.setOutros(summary.getOutros() + month.getOutros());
            summary.setTotal(summary.getTotal() + month.getTotal());
        }

        return summary;
    }

    @Cacheable(
            value = "compare-month",
            key = "#year1 + '-' + #month1 + '/' + #year2 + '-' + #month2"
    )
    public String compareWithOtherMonth(String year1, String month1, String year2, String month2){
        Optional<MonthlyAnalysis> analysis1 = repository.findByYearAndMonth(year1, month1);
        Optional<MonthlyAnalysis> analysis2 = repository.findByYearAndMonth(year2, month2);

        if (analysis1.isEmpty() || analysis2.isEmpty()){
            throw new RuntimeException("Não existe fatura para um dos meses informados");
        }

        try{
            String prompt = String.format("""
                            Você é um consultor financeiro.
                            
                            Compare os dois meses abaixo.
                            
                            Mês 1:
                            %s
                            
                            Mês 2:
                            %s
                            
                            Retorne apenas:
                            
                            Resumo:
                            - ...
                            
                            Mudanças:
                            - ...
                            
                            Economia:
                            - ...
                            
                            Regras:
                            - Máximo de 180 palavras.
                            - Máximo de 3 dicas.
                            - Seja objetivo.
                            - Não use linguagem motivacional.
                            - Não repita valores desnecessariamente.
                            """,
                    objectMapper.writeValueAsString(analysis1),
                    objectMapper.writeValueAsString(analysis2));

            ChatResponse response = gpt.callingGPTapi(prompt);

            return response.getResult().getOutput().getText();
        }catch (JsonProcessingException e){
            throw new RuntimeException("Erro ao converter resposta da IA.", e);
        }

    }
}
