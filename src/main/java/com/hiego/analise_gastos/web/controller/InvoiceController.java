package com.hiego.analise_gastos.web.controller;

import com.hiego.analise_gastos.core.entity.Invoice;
import com.hiego.analise_gastos.core.service.InvoiceService;
import com.hiego.analise_gastos.core.service.utils.CategoryAnalysis;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
public class InvoiceController {

    private final InvoiceService service;

    public InvoiceController(InvoiceService service) {
        this.service = service;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadData(@RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(service.upload(file));
    }

    @GetMapping("/analise")
    public ResponseEntity<CategoryAnalysis> analiseInvoice(@RequestParam String year,
                                                           @RequestParam String month){
        return ResponseEntity.ok(service.analyzeInvoicesByMonth(year, month));
    }

    @GetMapping("/month")
    public ResponseEntity<List<Invoice>> getInvoicesByMonth(
            @RequestParam String year,
            @RequestParam String month) {

        List<Invoice> invoices = service.findInvoicesByMonth(year, month);
        return ResponseEntity.ok(invoices);
    }


}
