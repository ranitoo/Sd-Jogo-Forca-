package com.example.demo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;




@Controller
@RequestMapping("/carrinho")
public class CarrinhoController {
	

	private ItemVendaRepository itemVendaRepo;
	private FaturaRepository faturaRepo;
	private VendaRepository vendaRepo;



    private final CarrinhoService carrinhoService;
    private final ProdutoRepository produtoRepo;
    private final ClienteRepository clienteRepo;

    public CarrinhoController(
            CarrinhoService carrinhoService, 
            ProdutoRepository produtoRepo,
            ItemVendaRepository itemVendaRepo,
            FaturaRepository faturaRepo,
            ClienteRepository clienteRepo,
            VendaRepository vendaRepo) {
        this.carrinhoService = carrinhoService;
        this.produtoRepo = produtoRepo;
        this.itemVendaRepo = itemVendaRepo;
        this.faturaRepo = faturaRepo;
        this.clienteRepo = clienteRepo;
        this.vendaRepo = vendaRepo;
    }

    @PostMapping("/adicionar")
    @ResponseBody
    public String adicionar(
        @RequestParam("produtoId") Long produtoId,
        @RequestParam("quantidade") int quantidade) {

        Produto produto = produtoRepo.findById(produtoId).orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        carrinhoService.adicionarProduto(produto, quantidade);
        return "OK";
    }


    @GetMapping
    public String verCarrinho(Model model) {
        model.addAttribute("itens", carrinhoService.listarItens());
        model.addAttribute("total", carrinhoService.calcularTotal());
        return "carrinho"; // carrinho.html
    }
    
    @GetMapping("/count")
    @ResponseBody
    public int contarItens() {
        return carrinhoService.listarItens().stream()
                              .mapToInt(CarrinhoItem::getQuantidade)
                              .sum();
    }
    
    @PostMapping("/diminuir")
    @ResponseBody
    public String diminuirQuantidade(@RequestParam("produtoId") Long produtoId) {
        carrinhoService.diminuirQuantidadeProduto(produtoId);
        return "OK";
    }

    @PostMapping("/remover")
    @ResponseBody
    public String removerProduto(@RequestParam("produtoId") Long produtoId) {
        carrinhoService.removerProduto(produtoId);
        return "OK";
    }



    @PostMapping("/pagar")
    public String pagar(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        /*Cliente cliente = clienteRepo.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));*/
        Cliente cliente = clienteRepo.findByEmail(userDetails.getUsername());
		if (cliente == null) {
			throw new RuntimeException("Cliente não encontrado");
		}

        Venda venda = new Venda();
        venda.setCliente(cliente);
        venda.setDataVenda(LocalDateTime.now());
        venda.setEstado("Pago");
        BigDecimal total = carrinhoService.calcularTotal();
        venda.setTotal(total);

        venda = vendaRepo.save(venda);

        for (CarrinhoItem item : carrinhoService.listarItens()) {
            Produto produto = item.getProduto();

            if (produto.getStock() < item.getQuantidade()) {
                throw new RuntimeException("Stock insuficiente para: " + produto.getNome());
            }

            produto.setStock(produto.getStock() - item.getQuantidade());
            produtoRepo.save(produto);

            ItemVenda itemVenda = new ItemVenda();
            itemVenda.setVenda(venda);
            itemVenda.setProduto(produto);
            itemVenda.setQuantidade(item.getQuantidade());
            itemVenda.setPrecoUnitario(produto.getPreco());
            itemVenda.setSubtotal(produto.getPreco().multiply(BigDecimal.valueOf(item.getQuantidade())));

            itemVendaRepo.save(itemVenda);

            venda.getItens().add(itemVenda);
        }

        Fatura fatura = new Fatura();
        fatura.setVenda(venda);
        fatura.setDataEmissao(LocalDateTime.now());
        fatura.setValorTotal(total);
        fatura.setMetodoPagamento("Contra Reembolso");

        faturaRepo.save(fatura);

        venda.setFatura(fatura);
        vendaRepo.save(venda);

        carrinhoService.limparCarrinho();

        return "redirect:/fatura/" + fatura.getId();
    }
    
}