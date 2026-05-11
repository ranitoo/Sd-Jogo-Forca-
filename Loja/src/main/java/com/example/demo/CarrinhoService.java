package com.example.demo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

@Service
@SessionScope  // Importante: 1 carrinho por sessão de utilizador
public class CarrinhoService {

    private List<CarrinhoItem> itens = new ArrayList<>();

    public void adicionarProduto(Produto produto, int quantidade) {
        for (CarrinhoItem item : itens) {
            if (item.getProduto().getId().equals(produto.getId())) {
                item.setQuantidade(item.getQuantidade() + quantidade);
                return;
            }
        }
        itens.add(new CarrinhoItem(produto, quantidade));
    }

    public void removerProduto(Long produtoId) {
        itens.removeIf(item -> item.getProduto().getId().equals(produtoId));
    }

    public List<CarrinhoItem> listarItens() {
        return itens;
    }

    public BigDecimal calcularTotal() {
        return itens.stream()
                .map(item -> item.getProduto().getPreco().multiply(BigDecimal.valueOf(item.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void limparCarrinho() {
        itens.clear();
    }
    

    public void diminuirQuantidadeProduto(Long produtoId) {
        for (int i = 0; i < itens.size(); i++) {
            CarrinhoItem item = itens.get(i);
            if (item.getProduto().getId().equals(produtoId)) {
                if (item.getQuantidade() > 1) {
                    item.setQuantidade(item.getQuantidade() - 1);
                } else {
                    itens.remove(i);
                }
                break;
            }
        }
    }

}