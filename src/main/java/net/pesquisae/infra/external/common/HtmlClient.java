package net.pesquisae.infra.external.common;

import org.jsoup.nodes.Document;

import java.io.IOException;


public interface HtmlClient {
    Document getResultados(String query) throws IOException;
}
