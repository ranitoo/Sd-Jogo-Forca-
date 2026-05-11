package com.example.demo;

public class CarrinhoItem {
    public CarrinhoItem(Produto produto, int quantidade) {
		super();
		this.produto = produto;
		this.quantidade = quantidade;
	}
	private Produto produto;
    private int quantidade;
	public Produto getProduto() {
		return produto;
	}
	public void setProduto(Produto produto) {
		this.produto = produto;
	}
	public int getQuantidade() {
		return quantidade;
	}
	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}

    // Getters, setters, total (produto.preco * quantidade)
}
