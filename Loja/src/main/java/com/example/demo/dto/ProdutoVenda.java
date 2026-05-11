package com.example.demo.dto;

public class ProdutoVenda {
    private String nome;
    private Long quantidadeVendida;

    public ProdutoVenda(String nome, Long quantidadeVendida) {
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