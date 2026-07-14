package com.hiego.analise_gastos.core.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hiego.analise_gastos.core.entity.Invoice;
import com.hiego.analise_gastos.core.repository.InvoiceRepository;
import com.hiego.analise_gastos.core.service.utils.CategoryAnalysis;
import com.univocity.parsers.common.record.Record;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import org.modelmapper.ModelMapper;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class InvoiceService {

    private final InvoiceRepository repository;
    private final ChatModel chatModel;
    private final MonthlyAnalysisService monthlyAnalysisService;

    public InvoiceService(InvoiceRepository repository, ChatModel chatModel, MonthlyAnalysisService monthlyAnalysisService) {
        this.repository = repository;
        this.chatModel = chatModel;
        this.monthlyAnalysisService = monthlyAnalysisService;
    }

    public String upload(MultipartFile file) throws IOException {
        List<Invoice> invoiceList = new ArrayList<>();
        InputStream inputStream = file.getInputStream();
        CsvParserSettings setting = new CsvParserSettings();

        setting.setHeaderExtractionEnabled(true);
        setting.setDelimiterDetectionEnabled(true, ',', ';');
        setting.setLineSeparatorDetectionEnabled(true);
        setting.setSkipEmptyLines(true);
        CsvParser parser = new CsvParser(setting);

        List<Record> parseAllRecords = parser.parseAllRecords(inputStream);

        if(parseAllRecords.isEmpty()){
            return "Nenhum registro encontrado no CSV!";
        }

        parseAllRecords.forEach( record -> {
            Invoice invoice = new Invoice();
            invoice.setDate(record.getString("date"));
            invoice.setTitle(record.getString("title"));
            invoice.setAmount(Float.parseFloat(record.getString("amount")));
            invoiceList.add(invoice);
        });

        repository.saveAll(invoiceList);

        return "Upload concluído com sucesso! Registros salvos: " + invoiceList.size();
    }

    public List<Invoice> findInvoicesByMonth(String year, String month) {
        return repository.findByYearAndMonth(year, month);
    }

    @Cacheable(
            value = "monthly-analysis",
            key = "#year + '-' + #month"
    )
    public CategoryAnalysis analyzeInvoicesByMonth(String year, String month) {
        List<Invoice> invoices = findInvoicesByMonth(year, month);

        if (invoices.isEmpty()) {
            throw new RuntimeException("Nenhuma fatura encontrada para o mês " + month + "/" + year);
        }

        return analiseInvoiceWithAi(invoices, year, month);
    }


    private CategoryAnalysis analiseInvoiceWithAi(List<Invoice> invoices, String year, String month) {
        String invoicesData = prepareInvoicesData(invoices);
        double totalAmount = calculateTotalAmount(invoices);

        try {
            String prompt = String.format(
                    """
                            Analise as despesas abaixo.
                            
                            %s
                            
                            Total gasto no mês: R$ %.2f
                            
                            Retorne APENAS um JSON válido, sem markdown, sem explicações e sem comentários.
                            
                            O JSON deve seguir exatamente este formato:
                            
                            {
                              "data": "%s-%s",
                              "entretenimento": {
                                "valor": 0,
                                "transacoes": []
                              },
                              "alimentacao": {
                                "valor": 0,
                                "transacoes": []
                              },
                              "estudos": {
                                "valor": 0,
                                "transacoes": []
                              },
                              "saude": {
                                "valor": 0,
                                "transacoes": []
                              },
                              "transporte": {
                                "valor": 0,
                                "transacoes": []
                              },
                              "outros": {
                                "valor": 0,
                                "transacoes": []
                              }
                            }
                            
                            Regras:
                            - O campo "data" deve conter o mês e ano analisados no formato MM/yyyy.
                            - O campo "valor" deve ser um número decimal.
                            - As transações devem conter apenas a descrição da compra.
                            - Toda compra deve pertencer a exatamente uma categoria.
                            - A soma dos valores de todas as categorias deve ser igual ao total gasto.
                            - Responda apenas com o JSON.
                            """,
                    invoicesData,
                    totalAmount,
                    year,
                    month
            );

            ChatResponse response = chatModel.call(
                    new Prompt(
                            prompt,
                            OpenAiChatOptions.builder()
                                    .model("gpt-4o")
                                    .temperature(0.4)
                                    .build()
                    ));

            String json = response.getResult().getOutput().getText();

            ObjectMapper mapper = new ObjectMapper();

            CategoryAnalysis categoryAnalysis = mapper.readValue(json, CategoryAnalysis.class);

            if(monthlyAnalysisService.getByMonthAndYear(year, month) == null) {
                monthlyAnalysisService.save(categoryAnalysis);
            }

            return categoryAnalysis;
        } catch (JsonProcessingException e){
            throw new RuntimeException("Erro ao converter resposta da IA.", e);
        }
    }

    private String prepareInvoicesData(List<Invoice> invoices) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < invoices.size(); i++) {
            Invoice invoice = invoices.get(i);
            sb.append(String.format("%d. Data: %s | Título: %s | Valor: R$ %.2f\n",
                    i + 1, invoice.getDate(), invoice.getTitle(), invoice.getAmount()));
        }

        return sb.toString();
    }

    private double calculateTotalAmount(List<Invoice> invoices) {
        return invoices.stream()
                .mapToDouble(Invoice::getAmount)
                .sum();
    }
}
