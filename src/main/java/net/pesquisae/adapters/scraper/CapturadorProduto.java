package net.pesquisae.adapters.scraper;

import net.pesquisae.domain.usecases.dto.CapturarProdutoDTO;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;

public interface CapturadorProduto {
    List<CapturarProdutoDTO> buscar(String query, Integer pagina) throws IOException;
}
