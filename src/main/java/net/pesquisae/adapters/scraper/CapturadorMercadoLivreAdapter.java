package net.pesquisae.adapters.scraper;

import net.pesquisae.adapters.mappers.CapturadorMercadoLivreMapper;
import net.pesquisae.domain.usecases.dto.CapturarProdutoDTO;
import net.pesquisae.infra.external.mercadolivre.MercadoLivreClientImpl;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class CapturadorMercadoLivreAdapter implements CapturadorProduto {
    private final MercadoLivreClientImpl mercadoLivreClientImpl;

    public CapturadorMercadoLivreAdapter(MercadoLivreClientImpl mercadoLivreClientImpl) {
        this.mercadoLivreClientImpl = mercadoLivreClientImpl;
    }

    @Override
    public List<CapturarProdutoDTO> buscar(String query) throws IOException {
        Document resultados = mercadoLivreClientImpl.getResultados(query);

        return extrair(resultados);
    }

    @Override
    public List<CapturarProdutoDTO> extrair(Document document) {
        List<CapturarProdutoDTO> listaCapturarProdutosDTO = new ArrayList<>();

        Elements items = document.select("ol.ui-search-layout")
                .first()
                .children()
                .select("[class=ui-search-layout__item]");

        for(Element item : items) {
            CapturarProdutoDTO capturarProdutoDTO = CapturadorMercadoLivreMapper.toDTO(item);
            if(capturarProdutoDTO == null) continue;

            listaCapturarProdutosDTO.add(capturarProdutoDTO);
        }

        return listaCapturarProdutosDTO;
    }
}
