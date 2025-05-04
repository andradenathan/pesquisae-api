package net.pesquisae.adapters.scraper;

import net.pesquisae.adapters.mappers.CapturadorAmazonMapper;
import net.pesquisae.domain.model.Marketplace;
import net.pesquisae.domain.usecases.cache.CacheService;
import net.pesquisae.domain.usecases.dto.CapturarProdutoDTO;
import net.pesquisae.domain.usecases.dto.ResultadoPaginaCapturador;
import net.pesquisae.infra.external.amazon.AmazonClientImpl;
import org.jsoup.nodes.Document;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class CapturadorAmazonAdapter extends CapturadorProdutoAdapter {
    private final AmazonClientImpl amazonClientImpl;
    private static final Logger logger = LoggerFactory.getLogger(CapturadorAmazonAdapter.class);

    public CapturadorAmazonAdapter(AmazonClientImpl amazonClientImpl,
                                   CacheService<ResultadoPaginaCapturador> cacheService) {
        super(cacheService);
        this.amazonClientImpl = amazonClientImpl;
    }

    @Override
    protected Document carregarPaginaInicial(String query) throws IOException {
        return amazonClientImpl.getResultados(Marketplace.AMAZON.getBaseUrl() + "/s?k=" + query);
    }

    @Override
    protected Document carregarPaginaPorUrl(String url) throws IOException {
        return amazonClientImpl.getResultados(url);
    }

    @Override
    protected Integer calcularTotalDePaginas(Document document) {
        Elements botoesPaginacao = document.select(".s-pagination-item").select(".s-pagination-disabled");
        int totalDePaginas = 1;
        for(Element botao : botoesPaginacao) {
            String textoBotao = botao.text();
            if (textoBotao.matches("\\d+")) {
                totalDePaginas = Integer.parseInt(textoBotao);
            }
        }

        return totalDePaginas;
    }

    @Override
    protected Elements extrairItens(Document document) {
        Elements itens = document.select(".s-result-item");
        if (!itens.isEmpty()) itens.remove(0);
        return itens;
    }

    @Override
    protected String construirUrlProximaPagina(Document document, Integer pagina) {
        Elements links = document.select(".a-list-item");
        for (Element elements : links) {
            Element link = elements.selectFirst("a");
            if (link != null && link.text().trim().equals(String.valueOf(pagina))) {
                return Marketplace.AMAZON.getBaseUrl() + link.attr("href");
            }
        }
        return null;
    }

    @Override
    protected CapturarProdutoDTO mapearItem(Element item) {
        return CapturadorAmazonMapper.toDTO(item);
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }

    @Override
    protected String recuperaChaveCache(String query, Integer pagina) {
        return "capturador_amazon:" + query + ":" + pagina;
    }
}
