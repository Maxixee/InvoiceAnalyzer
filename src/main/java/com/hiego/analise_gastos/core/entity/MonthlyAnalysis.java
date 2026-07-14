package com.hiego.analise_gastos.core.entity;

import com.hiego.analise_gastos.core.service.utils.Category;
import jakarta.persistence.*;

@Entity
@Table(name = "monthly_analysis")
public class MonthlyAnalysis {

    public MonthlyAnalysis() {
    }

    public MonthlyAnalysis(Long id, String date, Double entretenimento, Double alimentacao, Double estudos, Double saude, Double transporte, Double outros, Double total) {
        this.id = id;
        this.date = date;
        this.entretenimento = entretenimento;
        this.alimentacao = alimentacao;
        this.estudos = estudos;
        this.saude = saude;
        this.transporte = transporte;
        this.outros = outros;
        this.total = total;
    }

    public MonthlyAnalysis(String date, Double entretenimento, Double alimentacao, Double estudos, Double saude, Double transporte, Double outros, Double total) {
        this.date = date;
        this.entretenimento = entretenimento;
        this.alimentacao = alimentacao;
        this.estudos = estudos;
        this.saude = saude;
        this.transporte = transporte;
        this.outros = outros;
        this.total = total;
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "date")
    private String date;
    @Column(name = "entretenimento")
    private Double entretenimento;
    @Column(name = "alimentacao")
    private Double alimentacao;
    @Column(name = "estudos")
    private Double estudos;
    @Column(name = "saude")
    private Double saude;
    @Column(name = "transporte")
    private Double transporte;
    @Column(name = "outros")
    private Double outros;
    @Column(name = "total")
    private Double total;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getEntretenimento() {
        return entretenimento;
    }

    public void setEntretenimento(Double entretenimento) {
        this.entretenimento = entretenimento;
    }

    public Double getAlimentacao() {
        return alimentacao;
    }

    public void setAlimentacao(Double alimentacao) {
        this.alimentacao = alimentacao;
    }

    public Double getEstudos() {
        return estudos;
    }

    public void setEstudos(Double estudos) {
        this.estudos = estudos;
    }

    public Double getSaude() {
        return saude;
    }

    public void setSaude(Double saude) {
        this.saude = saude;
    }

    public Double getTransporte() {
        return transporte;
    }

    public void setTransporte(Double transporte) {
        this.transporte = transporte;
    }

    public Double getOutros() {
        return outros;
    }

    public void setOutros(Double outros) {
        this.outros = outros;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}
