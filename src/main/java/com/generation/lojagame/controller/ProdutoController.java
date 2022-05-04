package com.generation.lojagame.controller;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.generation.lojagame.model.Produto;
import com.generation.lojagame.repository.CategoriaRepository;
import com.generation.lojagame.repository.ProdutoRepository;

@RestController //controlador
@RequestMapping ("/produtos") //solicitação web
@CrossOrigin(origins ="*", allowedHeaders = "*") //restringe implementações
public class ProdutoController {
	
	@Autowired //conexão
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@GetMapping //solicitações get http
	public ResponseEntity<List<Produto>> getAll() //ResponseEntity -> respostas http -> cod status, cabeçalhos e corpo.
	{
		return ResponseEntity.ok(produtoRepository.findAll()); 
		// select * from produto
	}
	
	@GetMapping("/{idProduto}")
	public ResponseEntity<Produto> getById(@PathVariable Long idProduto)
	{
		return produtoRepository.findById(idProduto)
				.map(resposta -> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.notFound().build());
		
		//select * from produto where idProduto
	}
	
	@GetMapping("/nomeProduto/{nomeProduto}")
	public ResponseEntity<List<Produto>> getBynomeProduto(@PathVariable String nomeProduto)
	{
		return ResponseEntity.ok(produtoRepository.findAllBynomeProdutoContainingIgnoreCase(nomeProduto));
		
		//like %nomeProduto%
	}
	
	@PostMapping
	public ResponseEntity<Produto> postProduto(@Valid @RequestBody Produto produto){
		return categoriaRepository.findById(produto.getCategoria().getIdCategoria())
				.map(resposta -> ResponseEntity.status(HttpStatus.CREATED).body(produtoRepository.save(produto)))
				.orElse(ResponseEntity.badRequest().build());
	}
	
	@PutMapping
	public ResponseEntity<Produto> putProduto(@Valid @RequestBody Produto produto) {
					
		if (produtoRepository.existsById(produto.getIdProduto())){

			return categoriaRepository.findById(produto.getCategoria().getIdCategoria())
					.map(resposta -> ResponseEntity.status(HttpStatus.OK).body(produtoRepository.save(produto)))
					.orElse(ResponseEntity.badRequest().build());
		}		
		
		return ResponseEntity.notFound().build();

	}

	@DeleteMapping("/{idProduto}")
	public ResponseEntity<?> deleteProduto(@PathVariable Long idProduto) {
		
		return produtoRepository.findById(idProduto)
				.map(resposta -> {
					produtoRepository.deleteById(idProduto);
					return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
				})
				.orElse(ResponseEntity.notFound().build());
	}
	
	@GetMapping("/maior_preco/{preco}")
	public ResponseEntity<List<Produto>> getMaiorPreco(@PathVariable BigDecimal preco){ 
		return ResponseEntity.ok(produtoRepository.findByPrecoGreaterThanOrderByPreco(preco));
	}
	
	// Consulta pelo preço menor do que o preço digitado em ordem decrescente
	
	@GetMapping("/menor_preco/{preco}")
	public ResponseEntity<List<Produto>> getMenorPreco(@PathVariable BigDecimal preco){ 
		return ResponseEntity.ok(produtoRepository.findByPrecoLessThanOrderByPrecoDesc(preco));
	}
}
