package com.example.demo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class Fatura {
    public Fatura() {
		super();
	}
	public Fatura(Long id, Venda venda, LocalDateTime dataEmissao, BigDecimal valorTotal, String metodoPagamento) {
		super();
		this.id = id;
		this.venda = venda;
		this.dataEmissao = dataEmissao;
		this.valorTotal = valorTotal;
		this.metodoPagamento = metodoPagamento;
	}
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "venda_id", unique = true)
    private Venda venda;

    @Column(name = "data_emissao")
    private LocalDateTime dataEmissao;

    private BigDecimal valorTotal;
    private String metodoPagamento;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Venda getVenda() {
		return venda;
	}
	public void setVenda(Venda venda) {
		this.venda = venda;
	}
	public LocalDateTime getDataEmissao() {
		return dataEmissao;
	}
	public void setDataEmissao(LocalDateTime dataEmissao) {
		this.dataEmissao = dataEmissao;
	}
	public BigDecimal getValorTotal() {
		return valorTotal;
	}
	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}
	public String getMetodoPagamento() {
		return metodoPagamento;
	}
	public void setMetodoPagamento(String metodoPagamento) {
		this.metodoPagamento = metodoPagamento;
	}
}

