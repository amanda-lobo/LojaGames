package com.generation.lojagame.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.generation.lojagame.model.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long>{

	public List <Produto> findAllBynomeProdutoContainingIgnoreCase(@Param("produto")String produto);
	
	public List <Produto> findByPrecoGreaterThanOrderByPreco(BigDecimal preco);
	
	public List <Produto> findByPrecoLessThanOrderByPrecoDesc(BigDecimal preco);
}
