package com.example.demo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MainController {
	
	@Autowired
	private ClienteRepository clienteRepo;
	@Autowired
	private CategoriaRepository categoriaRepo;
	
	@GetMapping("")
	public String index() {
		return "index";
	}
	
	@GetMapping("/registar")
	public String mostrarFormRegisto(Model model) {
		model.addAttribute("cliente", new Cliente());
		return "registar_form";
	}
	
	@PostMapping("/processar_registo")
	public String processarRegisto(Cliente cliente) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(cliente.getPassword());
		cliente.setPassword(encodedPassword);
	    cliente.setDataRegisto(LocalDateTime.now());
	    cliente.setRole("ROLE_CLIENTE");
		
		clienteRepo.save(cliente);
		
	    return "registo_sucesso";  
		
	}
	
	@GetMapping("/inicio")
	public String inicio(Model model) {
	    List<Categoria> categorias = categoriaRepo.findAllWithProdutos();
	    model.addAttribute("categorias", categorias);
	    return "inicio";
	}
	
}
