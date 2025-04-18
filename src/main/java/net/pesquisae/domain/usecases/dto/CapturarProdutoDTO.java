package net.pesquisae.domain.usecases.dto;

import java.math.BigDecimal;

public record CapturarProdutoDTO(String nome, BigDecimal preco, String imageUrl) {}
