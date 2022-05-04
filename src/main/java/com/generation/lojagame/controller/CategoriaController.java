package com.generation.lojagame.controller;

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

import com.generation.lojagame.model.Categoria;
import com.generation.lojagame.repository.CategoriaRepository;

@RestController
@RequestMapping("/categoria")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CategoriaController {

	@Autowired
	private CategoriaRepository categoriaRepository;

	@GetMapping("/{idCategoria}")
	public ResponseEntity<Categoria> getById(@PathVariable Long idCategoria) {
		return categoriaRepository.findById(idCategoria).map(resposta -> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.notFound().build());
	}

	@GetMapping("/{nomeCategoria}")
	public ResponseEntity<List<Categoria>> getBynomeCategoria(@PathVariable String nomeCategoria) {
		return ResponseEntity.ok(categoriaRepository.findAllBynomeCategoriaContainingIgnoreCase(nomeCategoria));
	}

	@PostMapping
	public ResponseEntity<Categoria> postCategoria(@Valid @RequestBody Categoria categoria) {
		return ResponseEntity.status(HttpStatus.CREATED).body(categoriaRepository.save(categoria));
	}

	@PutMapping
	public ResponseEntity<Categoria> putCategoria(@Valid @RequestBody Categoria categoria) 
	{
		return categoriaRepository.findById(categoria.getIdCategoria())
				.map(resposta -> ResponseEntity.ok().body(categoriaRepository.save(categoria)))
				.orElse(ResponseEntity.notFound().build());
	}
	
	@DeleteMapping("/{idCategoria}")
	public ResponseEntity<?> deleteCategoria(@PathVariable Long idCategoria) {
		
		return categoriaRepository.findById(idCategoria)
				.map(resposta -> {
					categoriaRepository.deleteById(idCategoria);
					return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
				})
				.orElse(ResponseEntity.notFound().build());
	}

}
