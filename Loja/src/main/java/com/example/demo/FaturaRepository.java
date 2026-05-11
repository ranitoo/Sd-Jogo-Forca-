package com.example.demo;

import com.example.demo.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FaturaRepository extends JpaRepository<Fatura, Long> {
    Fatura findByVendaId(Long vendaId);
}