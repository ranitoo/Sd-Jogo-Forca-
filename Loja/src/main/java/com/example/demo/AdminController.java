package com.example.demo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.dto.ClienteEstatistica;
import com.example.demo.dto.ProdutoVenda;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
	
	@Autowired
	private ProdutoRepository produtoRepo;
	@Autowired
	private CategoriaRepository categoriaRepo;
	@Autowired
    private VendaRepository vendaRepo;
	
	@GetMapping("/admin_panel")
	public String adminPainel(Model model) {
		return "admin_panel";
	}
	
	//Categorias -----
	@GetMapping("admin/novacategoria")
    public String formNovaCategoria(Model model) {
        model.addAttribute("categoria", new Categoria());
        return "add_categorias";
    }

    @PostMapping("admin/novacategoria")
    public String salvarCategoria(@ModelAttribute Categoria categoria) {
        categoriaRepo.save(categoria);
        return "redirect:/admin_panel";
    }
    //-----
	
	@GetMapping("/admin/produtos")
	public String mostrarFormularioProduto(Model model) {
	    model.addAttribute("produto", new Produto());
	    model.addAttribute("categorias", categoriaRepo.findAll());
	    return "adicionar_produto";
	}
	
	@PostMapping("/admin/produtos")
    public String guardarProduto(@ModelAttribute Produto produto) {
        produtoRepo.save(produto);
        return "redirect:/admin_panel";
    }
	
	
	
	
	
	@GetMapping("/admin/estatisticas")
	public String mostrarEstatisticas(Model model) throws Exception {
	    LocalDate hoje = LocalDate.now();

	    LocalDateTime inicioDia = hoje.atStartOfDay();
	    LocalDateTime fimDia = hoje.atTime(LocalTime.MAX);

	    LocalDate inicioSemana = hoje.minusDays(hoje.getDayOfWeek().getValue() - 1);
	    LocalDateTime inicioSemanaDt = inicioSemana.atStartOfDay();
	    LocalDateTime fimSemanaDt = inicioSemana.plusDays(6).atTime(LocalTime.MAX);

	    LocalDate inicioMes = hoje.withDayOfMonth(1);
	    LocalDateTime inicioMesDt = inicioMes.atStartOfDay();
	    LocalDateTime fimMesDt = hoje.withDayOfMonth(hoje.lengthOfMonth()).atTime(LocalTime.MAX);

	    BigDecimal faturadoDia = vendaRepo.getTotalFaturadoEntre(inicioDia, fimDia);
	    BigDecimal faturadoSemana = vendaRepo.getTotalFaturadoEntre(inicioSemanaDt, fimSemanaDt);
	    BigDecimal faturadoMes = vendaRepo.getTotalFaturadoEntre(inicioMesDt, fimMesDt);

	    model.addAttribute("faturadoDia", faturadoDia != null ? faturadoDia : BigDecimal.ZERO);
	    model.addAttribute("faturadoSemana", faturadoSemana != null ? faturadoSemana : BigDecimal.ZERO);
	    model.addAttribute("faturadoMes", faturadoMes != null ? faturadoMes : BigDecimal.ZERO);

	    // Melhores clientes
	    List<ClienteEstatistica> melhoresClientes = vendaRepo.findMelhoresClientes();

	    List<String> nomesClientes = melhoresClientes.stream()
	            .map(ClienteEstatistica::getNome)
	            .collect(Collectors.toList());

	    List<BigDecimal> valoresGastos = melhoresClientes.stream()
	            .map(ClienteEstatistica::getTotalComprado)
	            .collect(Collectors.toList());

	    // Top 5 produtos mais vendidos
	    Pageable top5 = (Pageable) PageRequest.of(0, 5);
	    List<ProdutoVenda> topProdutos = vendaRepo.findTopProdutosMaisVendidos(top5);

	    List<String> nomesProdutos = topProdutos.stream()
	            .map(ProdutoVenda::getNome)
	            .collect(Collectors.toList());

	    List<Long> quantidadesVendidas = topProdutos.stream()
	            .map(ProdutoVenda::getQuantidadeVendida)
	            .collect(Collectors.toList());

	    ObjectMapper mapper = new ObjectMapper();

	    model.addAttribute("nomesClientesJson", mapper.writeValueAsString(nomesClientes));
	    model.addAttribute("valoresGastosJson", mapper.writeValueAsString(valoresGastos));
	    model.addAttribute("nomesProdutosJson", mapper.writeValueAsString(nomesProdutos));
	    model.addAttribute("quantidadesVendidasJson", mapper.writeValueAsString(quantidadesVendidas));
	    
	    
	    
	    
	    
	    
	    
	    
		
	    
	    
	    //Quanto cliente gastou
	    
	   /* List<ClienteEstatistica> quantogastou = vendaRepo.findCompraCliente();
	    
	    List<String> nomeClientes = quantogastou.stream()
	        .map(ClienteEstatistica::getNome)
	        .collect(Collectors.toList());
	    
	    List<BigDecimal> valoreGastos = quantogastou.stream()
	            .map(ClienteEstatistica::getTotalComprado)
	            .collect(Collectors.toList());
	    
	    
	    model.addAttribute("NomesClientes", mapper.writeValueAsString(nomeClientes));
	    model.addAttribute("ValorGasto",mapper.writeValueAsString(valoreGastos));*/
	    
	    
	   
	    
	    
	    

	    return "estatisticas";
	}



	
}