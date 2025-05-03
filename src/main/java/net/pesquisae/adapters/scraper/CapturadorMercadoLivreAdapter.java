package net.pesquisae.adapters.scraper;

import net.pesquisae.adapters.mappers.CapturadorMercadoLivreMapper;
import net.pesquisae.domain.model.Marketplace;
import net.pesquisae.domain.usecases.dto.CapturarProdutoDTO;
import net.pesquisae.infra.external.mercadolivre.MercadoLivreClientImpl;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class CapturadorMercadoLivreAdapter extends CapturadorProdutoAdapter {
    private final MercadoLivreClientImpl mercadoLivreClientImpl;
    private static final Logger logger = LoggerFactory.getLogger(CapturadorMercadoLivreAdapter.class);

    public CapturadorMercadoLivreAdapter(MercadoLivreClientImpl mercadoLivreClientImpl) {
        this.mercadoLivreClientImpl = mercadoLivreClientImpl;
    }

    @Override
    protected Document carregarPaginaInicial(String query) throws IOException {
        String url = Marketplace.MERCADO_LIVRE.getBaseUrl() + query;
        return mercadoLivreClientImpl.getResultados(url);
    }

    @Override
    protected Document carregarPaginaPorUrl(String url) throws IOException {
        return mercadoLivreClientImpl.getResultados(url);
    }

    @Override
    protected Elements extrairItens(Document document) {
        Element layout = document.selectFirst("ol.ui-search-layout");
        if (layout == null) return new Elements();
        return layout.children().select("[class=ui-search-layout__item]");
    }

    @Override
    protected String construirUrlProximaPagina(Document document, Integer pagina) {
        String urlAtual = document.select("meta[property=og:url]").attr("content");

        if (pagina == 1) return urlAtual;

        int offset = 49 + (pagina - 2) * 48;
        String urlSemOffset = urlAtual.replaceAll("_Desde_\\d+", "");
        return urlSemOffset.replace("_NoIndex_True", "_Desde_" + offset + "_NoIndex_True");
    }

    @Override
    protected CapturarProdutoDTO mapearItem(Element item) {
        return CapturadorMercadoLivreMapper.toDTO(item);
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }
}
