package com.hiego.analise_gastos.core.service.utils;

public class CategoryAnalysis {

    public CategoryAnalysis() {
    }

    public CategoryAnalysis(String data, Category entretenimento, Category alimentacao, Category estudos, Category saude, Category transporte, Category outros) {
        this.data = data;
        this.entretenimento = entretenimento;
        this.alimentacao = alimentacao;
        this.estudos = estudos;
        this.saude = saude;
        this.transporte = transporte;
        this.outros = outros;
    }

    private String data;
    private Category entretenimento;
    private Category alimentacao;
    private Category estudos;
    private Category saude;
    private Category transporte;
    private Category outros;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Category getEntretenimento() {
        return entretenimento;
    }

    public void setEntretenimento(Category entretenimento) {
        this.entretenimento = entretenimento;
    }

    public Category getAlimentacao() {
        return alimentacao;
    }

    public void setAlimentacao(Category alimentacao) {
        this.alimentacao = alimentacao;
    }

    public Category getEstudos() {
        return estudos;
    }

    public void setEstudos(Category estudos) {
        this.estudos = estudos;
    }

    public Category getSaude() {
        return saude;
    }

    public void setSaude(Category saude) {
        this.saude = saude;
    }

    public Category getTransporte() {
        return transporte;
    }

    public void setTransporte(Category transporte) {
        this.transporte = transporte;
    }

    public Category getOutros() {
        return outros;
    }

    public void setOutros(Category outros) {
        this.outros = outros;
    }
}
