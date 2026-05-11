package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class FaturaController {

    @Autowired
    private FaturaRepository faturaRepository;

    @GetMapping("/fatura/{id}")
    public String mostrarFatura(@PathVariable("id") Long id, Model model) {

        Fatura fatura = faturaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fatura não encontrada"));

        Venda venda = fatura.getVenda();
        List<ItemVenda> itens = venda.getItens();

        model.addAttribute("fatura", fatura);
        model.addAttribute("venda", venda);
        model.addAttribute("itens", itens);

        return "fatura";
    }
}