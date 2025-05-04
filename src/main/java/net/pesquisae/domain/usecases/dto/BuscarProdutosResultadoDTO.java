package net.pesquisae.domain.usecases.dto;

import java.util.List;

public record BuscarProdutosResultadoDTO(List<CapturarProdutoDTO> produtos, Integer total, Integer totalDePaginas) {}
