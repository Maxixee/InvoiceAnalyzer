package com.hiego.analise_gastos.core.service.utils;

import java.util.List;

public class Category {

    public Category() {
    }

    public Category(Double valor, List<String> transacoes) {
        this.valor = valor;
        this.transacoes = transacoes;
    }

    private Double valor;
    private List<String> transacoes;

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public List<String> getTransacoes() {
        return transacoes;
    }

    public void setTransacoes(List<String> transacoes) {
        this.transacoes = transacoes;
    }
}
