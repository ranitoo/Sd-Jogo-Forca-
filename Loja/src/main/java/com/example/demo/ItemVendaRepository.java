package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ItemVendaRepository extends JpaRepository<ItemVenda, Long> {

    @Query("SELECT iv.produto.nome, SUM(iv.quantidade) FROM ItemVenda iv GROUP BY iv.produto.nome ORDER BY SUM(iv.quantidade) DESC")
    List<Object[]> findProdutosMaisVendidos();

    @Query("SELECT iv.produto.nome, SUM(iv.quantidade) FROM ItemVenda iv GROUP BY iv.produto.nome ORDER BY SUM(iv.quantidade) ASC")
    List<Object[]> findProdutosMenosVendidos();
}