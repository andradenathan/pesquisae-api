package net.pesquisae.adapters.scraper;

import net.pesquisae.domain.usecases.dto.ResultadoPaginaCapturador;

import java.io.IOException;
import java.util.List;

public interface CapturadorProduto {
    ResultadoPaginaCapturador buscar(String query, Integer pagina) throws IOException;
}
