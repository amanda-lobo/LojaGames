package com.generation.lojagame.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.generation.lojagame.model.Categoria;


public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
	
	public List<Categoria> findAllBynomeCategoriaContainingIgnoreCase(@Param("categoria")String Categoria);

}
