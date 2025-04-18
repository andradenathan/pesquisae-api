package net.pesquisae.domain.model;

public enum Marketplace {
    AMAZON("https://www.amazon.com.br/s?k=", "Amazon"),
    MERCADO_LIVRE("https://lista.mercadolivre.com.br/", "Mercado Livre");

    private final String baseUrl;
    private final String nome;

    Marketplace(String baseUrl, String nome) {
        this.baseUrl = baseUrl;
        this.nome = nome;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getNome() {
        return nome;
    }
}
