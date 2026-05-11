package com.example.demo;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.dto.ClienteEstatistica;
import com.example.demo.dto.ProdutoVenda;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface VendaRepository extends JpaRepository<Venda, Long> {

    List<Venda> findByClienteId(Long clienteId);

    List<Venda> findByDataVendaBetween(LocalDateTime start, LocalDateTime end);

    // 🔹 Total faturado entre duas datas
    @Query("SELECT SUM(v.total) FROM Venda v WHERE v.dataVenda BETWEEN :start AND :end")
    BigDecimal getTotalFaturadoEntre(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    // 🔹 Melhores clientes (por valor total gasto)
    @Query("SELECT new com.example.demo.dto.ClienteEstatistica(v.cliente.nome, SUM(v.total)) " +
    	       "FROM Venda v GROUP BY v.cliente.nome ORDER BY SUM(v.total) DESC")
    	List<ClienteEstatistica> findMelhoresClientes();
    
    @Query("SELECT new com.example.demo.dto.ProdutoVenda(i.produto.nome, SUM(i.quantidade)) " +
    	       "FROM Venda v JOIN v.itens i " +
    	       "GROUP BY i.produto.nome " +
    	       "ORDER BY SUM(i.quantidade) DESC")
    	List<ProdutoVenda> findTopProdutosMaisVendidos(Pageable pageable);
    	
    	/*@Query("SELECT new com.example.demo.dto.ClienteEstatistica(v.cliente.id,COMPARE(v.total))"+
    			"FROM Venda v GROUP BY v.cliente.id ORDER BY COMPARE(v.total) DESC")
    	List<ClienteEstatistica>findCompraCliente();*/
    



}