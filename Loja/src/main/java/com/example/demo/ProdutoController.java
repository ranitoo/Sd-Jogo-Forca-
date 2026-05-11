package com.example.demo;

import com.example.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @GetMapping
    public List<Produto> listarProdutos() {
        return produtoRepository.findAll();
    }

    @GetMapping("/categoria/{categoriaId}")
    public List<Produto> listarPorCategoria(@PathVariable Long categoriaId) {
        return produtoRepository.findByCategoriaId(categoriaId);
    }

    @PostMapping
    public ResponseEntity<Produto> criarProduto(@RequestBody Produto produto) {
        if (produto.getCategoria() != null && produto.getCategoria().getId() != null) {
            categoriaRepository.findById(produto.getCategoria().getId()).ifPresent(produto::setCategoria);
        } else {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(produtoRepository.save(produto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> obterProduto(@PathVariable Long id) {
        return produtoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Produto> atualizarProduto(@PathVariable Long id, @RequestBody Produto novoProduto) {
        return produtoRepository.findById(id).map(produto -> {
            produto.setNome(novoProduto.getNome());
            produto.setDescricao(novoProduto.getDescricao());
            produto.setPreco(novoProduto.getPreco());
            produto.setStock(novoProduto.getStock());
            if (novoProduto.getCategoria() != null) {
                categoriaRepository.findById(novoProduto.getCategoria().getId())
                    .ifPresent(produto::setCategoria);
            }
            return ResponseEntity.ok(produtoRepository.save(produto));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> eliminarProduto(@PathVariable Long id) {
        return produtoRepository.findById(id).map(produto -> {
            produtoRepository.delete(produto);
            return ResponseEntity.noContent().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}