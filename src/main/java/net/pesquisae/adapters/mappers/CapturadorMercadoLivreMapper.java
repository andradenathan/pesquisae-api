package net.pesquisae.adapters.mappers;

import net.pesquisae.domain.model.Marketplace;
import net.pesquisae.domain.usecases.dto.CapturarProdutoDTO;
import org.jsoup.nodes.Element;

import java.math.BigDecimal;

public class CapturadorMercadoLivreMapper {
    public static CapturarProdutoDTO toDTO(Element item) {
        try {
            String nome = item.select(".poly-component__title").text();
            String preco = item.select(".poly-price__current").select(".andes-money-amount__fraction").text();
            BigDecimal precoFormatado = new BigDecimal(preco);
            String imageUrl = item.select(".poly-component__picture").attr("data-src");
            String link = item.select(".poly-component__title").attr("href");

            return new CapturarProdutoDTO(nome, precoFormatado, imageUrl, link, Marketplace.MERCADO_LIVRE.getNome());
        } catch (Exception e) {
            return null;
        }
    }
}
