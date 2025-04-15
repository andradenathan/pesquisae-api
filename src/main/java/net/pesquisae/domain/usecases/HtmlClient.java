package net.pesquisae.domain.usecases;

import org.jsoup.nodes.Document;

public interface HtmlClient {
    Document getResultados(String query);
}
