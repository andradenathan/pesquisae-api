package net.pesquisae.infra.external.mercadolivre;

import net.pesquisae.domain.usecases.HtmlClient;
import org.jsoup.nodes.Document;

public class MercadoLivreClient implements HtmlClient {
    @Override
    public Document getResultados(String query) {
        try {
            String url = "https://lista.mercadolivre.com.br/" + query;
            return org.jsoup.Jsoup.connect(url)
                    .timeout(10000)
                    .userAgent("Mozilla/5.0")
                    .get();
        } catch (Exception e) {
            String message =
                    String.format("Erro ao buscar resultados do Mercado Livre para o produto [%s]: %s", query, e.getMessage());

            throw new RuntimeException(message, e);
        }
    }
}
