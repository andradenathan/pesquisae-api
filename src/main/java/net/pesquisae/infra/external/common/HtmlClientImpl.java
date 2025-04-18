package net.pesquisae.infra.external.common;

import net.pesquisae.domain.model.Marketplace;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class HtmlClientImpl implements HtmlClient {
    private final Marketplace marketplace;

    public HtmlClientImpl(Marketplace marketplace) {
        this.marketplace = marketplace;
    }

    @Override
    public Document getResultados(String query) {
        String url = marketplace.getBaseUrl() + query;
        try {
            return Jsoup.connect(url)
                    .timeout(10000)
                    .userAgent("Mozilla/5.0")
                    .get();
        } catch (Exception e) {
            String message = String.format(
                    "Erro ao buscar resultados do serviço [%s] para o produto [%s]: %s",
                    marketplace.getNome(),
                    query,
                    e.getMessage()
            );
            throw new RuntimeException(message, e);
        }
    }
}