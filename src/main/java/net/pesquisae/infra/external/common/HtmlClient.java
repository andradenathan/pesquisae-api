package net.pesquisae.infra.external.common;

import org.jsoup.nodes.Document;

public interface HtmlClient {
    Document getResultados(String query);
}
