package com.hiego.analise_gastos.core.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hiego.analise_gastos.core.entity.MonthlyAnalysis;
import com.hiego.analise_gastos.core.repository.MontlyAnalysisRepository;
import com.hiego.analise_gastos.core.service.mapper.MonthlyAnalysisMapper;
import com.hiego.analise_gastos.core.service.utils.CategoryAnalysis;
import org.springframework.ai.chat.model.ChatResponse;
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
