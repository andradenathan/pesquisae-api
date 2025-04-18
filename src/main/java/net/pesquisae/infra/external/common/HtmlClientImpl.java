package net.pesquisae.infra.external.common;

import net.pesquisae.domain.model.Marketplace;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

import java.io.IOException;
import java.net.SocketTimeoutException;

public class HtmlClientImpl implements HtmlClient {
    private final Marketplace marketplace;
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36";
    private static final int TIMEOUT_MS = 10000;

    public HtmlClientImpl(Marketplace marketplace) {
        this.marketplace = marketplace;
    }

    @Override
    @Retryable(
            value = { SocketTimeoutException.class, IOException.class },
            maxAttempts = 5,
            backoff = @Backoff(delay = 1000)

    )
    public Document getResultados(String query) throws IOException {
        String url = marketplace.getBaseUrl() + query;
        try {
            return Jsoup.connect(url)
                    .timeout(TIMEOUT_MS)
                    .userAgent(USER_AGENT)
                    .get();
        } catch (SocketTimeoutException e) {
            String message = String.format(
                    "Timeout ao buscar resultados do serviço [%s] para o produto [%s]: %s",
                    marketplace.getNome(),
                    query,
                    e.getMessage()
            );

            throw new SocketTimeoutException(message);
        } catch (IOException e) {
            String message = String.format(
                    "Erro de I/O ao buscar resultados do serviço [%s] para o produto [%s]: %s",
                    marketplace.getNome(),
                    query,
                    e.getMessage()
            );

            throw new IOException(message);
        }
        catch (Exception e) {
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