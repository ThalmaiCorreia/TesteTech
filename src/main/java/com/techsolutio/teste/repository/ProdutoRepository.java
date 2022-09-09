package com.techsolutio.teste.repository;

import com.techsolutio.teste.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    public List<Produto> findAllByNomeContainingIgnoreCase(@Param("nome")String nome);

    Optional<Produto> findByFornecedor(String fornecedor);

}
