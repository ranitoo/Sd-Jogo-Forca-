package com.example.demo.dto;

public class ProdutoEstatistica {
    private String nome;
    private Long quantidadeVendida;

    public ProdutoEstatistica(String nome, Long quantidadeVendida) {
        this.nome = nome;
        this.quantidadeVendida = quantidadeVendida;
    }

    public String getNome() {
        return nome;
    }

    public Long getQuantidadeVendida() {
        return quantidadeVendida;
    }
}