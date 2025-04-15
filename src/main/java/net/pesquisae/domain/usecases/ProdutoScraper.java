package net.pesquisae.domain.usecases;

import net.pesquisae.domain.entities.Produto;

import java.util.List;

public interface ProdutoScraper {
    List<Produto> scrape(String query);
}
