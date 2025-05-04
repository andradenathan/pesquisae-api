package net.pesquisae.adapters.scraper;

import com.fasterxml.jackson.core.type.TypeReference;
import net.pesquisae.domain.usecases.cache.CacheService;
import net.pesquisae.domain.usecases.dto.CapturarProdutoDTO;
import net.pesquisae.domain.usecases.dto.ResultadoPaginaCapturador;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


public abstract class CapturadorProdutoAdapter implements CapturadorProduto {
    protected final CacheService<ResultadoPaginaCapturador> cacheService;

    protected CapturadorProdutoAdapter(CacheService<ResultadoPaginaCapturador> cacheService) {
        this.cacheService = cacheService;
    }

    @Override
    public ResultadoPaginaCapturador buscar(String query, Integer pagina) throws IOException {
        String chaveCache = recuperaChaveCache(query, pagina);

        Optional<ResultadoPaginaCapturador> resultadoPaginaCache = cacheService.get(chaveCache, new TypeReference<>() {});

        if(resultadoPaginaCache.isPresent()) {
            return resultadoPaginaCache.get();
        }


        Document doc = carregarPaginaInicial(query);

        Integer totalDePaginas = calcularTotalDePaginas(doc);

        Elements itens = buscarItensDaPagina(doc, pagina);

        List<CapturarProdutoDTO> resultados = itens.stream()
                .map(this::mapearItem)
                .filter(Objects::nonNull)
                .toList();


        ResultadoPaginaCapturador resultadoPaginaCapturador = new ResultadoPaginaCapturador(resultados, totalDePaginas);

        cacheService.put(chaveCache, resultadoPaginaCapturador);

        return resultadoPaginaCapturador;
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

    protected abstract Integer calcularTotalDePaginas(Document document);

    protected abstract Elements extrairItens(Document document);

    protected abstract String construirUrlProximaPagina(Document document, Integer pagina);

    protected abstract CapturarProdutoDTO mapearItem(Element item);

    protected abstract Logger getLogger();

    protected abstract String recuperaChaveCache(String query, Integer pagina);
}
