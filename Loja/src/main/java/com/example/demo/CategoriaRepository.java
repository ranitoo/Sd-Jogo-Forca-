package com.example.demo;

import com.example.demo.*;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
	@Query("SELECT DISTINCT c FROM Categoria c LEFT JOIN FETCH c.produtos")
    List<Categoria> findAllWithProdutos();
}