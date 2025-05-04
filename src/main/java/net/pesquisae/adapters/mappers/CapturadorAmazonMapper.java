package net.pesquisae.adapters.mappers;

import net.pesquisae.domain.model.Marketplace;
import net.pesquisae.domain.usecases.dto.CapturarProdutoDTO;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;

import java.math.BigDecimal;

public class CapturadorAmazonMapper {
    public static CapturarProdutoDTO toDTO(Element item) {
        try {
            String nome = item.children().select("h2").text().trim();

            String preco = item
                    .children()
                    .select(".a-price span.a-price-whole")
                    .text()
                    .replace(",", StringUtils.EMPTY);

            BigDecimal precoFormatado = new BigDecimal(preco);
            String imageUrl = item.children().select("img").attr("src");
            String link = Marketplace.AMAZON.getBaseUrl() + item.children().select("a").attr("href");

            return new CapturarProdutoDTO(nome, precoFormatado, imageUrl, link, Marketplace.AMAZON.getNome());
        } catch (Exception e) {
            return null;
        }
    }
}
