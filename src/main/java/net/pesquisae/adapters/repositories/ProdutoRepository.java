package net.pesquisae.adapters.repositories;

import net.pesquisae.domain.entities.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProdutoRepository extends JpaRepository<Produto, UUID> {}
