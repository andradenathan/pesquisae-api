package net.pesquisae.adapters.scraper;

import net.pesquisae.domain.entities.Produto;
import net.pesquisae.domain.usecases.ProdutoScraper;

import java.util.ArrayList;
import java.util.List;

public class MercadoLivreScraperAdapter implements ProdutoScraper {
    @Override
    public List<Produto> scrape(String query) {
        return new ArrayList<>();
    }
}
