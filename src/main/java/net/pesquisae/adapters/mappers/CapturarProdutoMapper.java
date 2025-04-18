package net.pesquisae.adapters.mappers;

import net.pesquisae.domain.usecases.dto.CapturarProdutoDTO;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;

import java.math.BigDecimal;

public class CapturarProdutoMapper {
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

            return new CapturarProdutoDTO(nome, precoFormatado, imageUrl);
        } catch (Exception e) {
            return null;
        }
    }
}
