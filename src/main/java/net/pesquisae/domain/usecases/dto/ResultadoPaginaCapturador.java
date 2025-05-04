package net.pesquisae.domain.usecases.dto;

import java.util.List;

public record ResultadoPaginaCapturador(List<CapturarProdutoDTO> produtos, Integer totalPaginas) {}
