package com.hiego.analise_gastos.core.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "invoice")
public class Invoice {

    public Invoice() {
    }

    public Invoice(Long id, String date, String title, float amount) {
        this.id = id;
        this.date = date;
        this.title = title;
        this.amount = amount;
    }

    public Invoice(String date, String title, float amount) {
        this.date = date;
        this.title = title;
        this.amount = amount;
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "date")
    private String date;
    @Column(name = "title")
    private String title;
    @Column(name = "amount")
    private float amount;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }
}
