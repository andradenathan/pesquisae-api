package net.pesquisae.infra.external.amazon;

import net.pesquisae.domain.usecases.HtmlClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class AmazonClient implements HtmlClient {
    @Override
    public Document getResultados(String query) {
        try {
            String url = "https://www.amazon.com.br/s?k=" + query;
            return Jsoup.connect(url)
                    .timeout(10000)
                    .userAgent("Mozilla/5.0")
                    .get();
        } catch (Exception e) {
            String message =
                    String.format("Erro ao buscar resultados da Amazon para o produto [%s]: %s", query, e.getMessage());

            throw new RuntimeException(message, e);
        }
    }
}
