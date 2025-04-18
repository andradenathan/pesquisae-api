package net.pesquisae.adapters.scraper;

import net.pesquisae.domain.usecases.dto.CapturarProdutoDTO;
import org.jsoup.nodes.Document;

import java.util.List;

public interface CapturadorProduto {
    List<CapturarProdutoDTO> buscar(String query);
    List<CapturarProdutoDTO> extrair(Document document);
}
