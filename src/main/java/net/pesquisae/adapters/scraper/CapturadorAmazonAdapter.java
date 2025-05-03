package net.pesquisae.adapters.scraper;

import net.pesquisae.adapters.mappers.CapturadorAmazonMapper;
import net.pesquisae.domain.model.Marketplace;
import net.pesquisae.domain.usecases.BuscarProdutosUseCase;
import net.pesquisae.domain.usecases.dto.CapturarProdutoDTO;
import net.pesquisae.infra.external.amazon.AmazonClientImpl;
import org.jsoup.nodes.Document;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;

import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class CapturadorAmazonAdapter implements CapturadorProduto {
    private final AmazonClientImpl amazonClientImpl;

    private static final Logger logger = LoggerFactory.getLogger(CapturadorAmazonAdapter.class);

    public CapturadorAmazonAdapter(AmazonClientImpl amazonClientImpl) {
        this.amazonClientImpl = amazonClientImpl;
    }

    @Override
    public List<CapturarProdutoDTO> buscar(String query, Integer pagina) throws IOException {
        String baseUrl = Marketplace.AMAZON.getBaseUrl() + "/s?k=" + query;

        Document resultados = amazonClientImpl.getResultados(baseUrl);

        return extrair(resultados, pagina);
    }

    private List<CapturarProdutoDTO> extrair(Document resultados, Integer pagina) {
        Elements itens = buscarItensDaPagina(resultados, pagina);

        return itens.stream()
                .map(CapturadorAmazonMapper::toDTO)
                .filter(Objects::nonNull)
                .toList();
    }

    private Elements buscarItensDaPagina(Document document, Integer pagina) {
        if (pagina == 1) {
            return extrairItens(document);
        }

        String urlProximaPagina = extrairUrlProximaPagina(document, pagina);
        if (urlProximaPagina == null) {
            logger.warn("Página {} não encontrada nos resultados da Amazon", pagina);
            return new Elements();
        }

        try {
            Document novaPagina = amazonClientImpl.getResultados(urlProximaPagina);
            return extrairItens(novaPagina);
        } catch (IOException ioException) {
            logger.error("Erro ao carregar a página {} da Amazon: {}", pagina, ioException.getMessage());
            return new Elements();
        }
    }

    private String extrairUrlProximaPagina(Document document, Integer pagina) {
        Elements linksDePaginacao = document.select(".a-list-item");

        for (Element elemento : linksDePaginacao) {
            Element link = elemento.selectFirst("a");
            if (link != null && link.text().trim().equals(String.valueOf(pagina))) {
                return Marketplace.AMAZON.getBaseUrl() + link.attr("href");
            }
        }

        return null;
    }

    private Elements extrairItens(Document document) {
        Elements itens = document.select(".s-result-item");
        if (!itens.isEmpty()) {
            itens.remove(0);
        }
        return itens;
    }
}
