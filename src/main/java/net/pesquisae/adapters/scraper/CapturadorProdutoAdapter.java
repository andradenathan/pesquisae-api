package net.pesquisae.adapters.scraper;

import net.pesquisae.domain.usecases.dto.CapturarProdutoDTO;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.List;
import java.util.Objects;


public abstract class CapturadorProdutoAdapter implements CapturadorProduto {
    @Override
    public List<CapturarProdutoDTO> buscar(String query, Integer pagina) throws IOException {
        Document doc = carregarPaginaInicial(query);

        Elements itens = buscarItensDaPagina(doc, pagina);
        return itens.stream()
                .map(this::mapearItem)
                .filter(Objects::nonNull)
                .toList();
    }

    private Elements buscarItensDaPagina(Document document, Integer pagina) throws IOException {
        if (pagina == 1) {
            return extrairItens(document);
        }

        String urlProximaPagina = construirUrlProximaPagina(document, pagina);
        if (urlProximaPagina == null) {
            getLogger().warn("Página {} não encontrada", pagina);
            return new Elements();
        }

        Document novaPagina = carregarPaginaPorUrl(urlProximaPagina);
        return extrairItens(novaPagina);
    }

    protected abstract Document carregarPaginaInicial(String query) throws IOException;

    protected abstract Document carregarPaginaPorUrl(String url) throws IOException;

    protected abstract Elements extrairItens(Document document);

    protected abstract String construirUrlProximaPagina(Document document, Integer pagina);

    protected abstract CapturarProdutoDTO mapearItem(Element item);

    protected abstract Logger getLogger();
}
