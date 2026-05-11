package com.example.demo.dto;

import java.math.BigDecimal;

public class ClienteEstatistica {
    private String nome;
    private BigDecimal totalComprado;

    public ClienteEstatistica(String nome, BigDecimal totalComprado) {
        this.nome = nome;
        this.totalComprado = totalComprado;
    }

    public BigDecimal getTotalComprado() {
        return totalComprado;
    }

    public String getNome() {
        return nome;
    }

}
