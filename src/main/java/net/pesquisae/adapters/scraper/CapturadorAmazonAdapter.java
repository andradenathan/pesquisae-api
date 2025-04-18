package net.pesquisae.adapters.scraper;

import net.pesquisae.adapters.mappers.CapturarProdutoMapper;
import net.pesquisae.domain.usecases.dto.CapturarProdutoDTO;
import net.pesquisae.infra.external.amazon.AmazonClientImpl;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CapturadorAmazonAdapter implements CapturadorProduto {
    private final AmazonClientImpl amazonClientImpl;

    public CapturadorAmazonAdapter(AmazonClientImpl amazonClientImpl) {
        this.amazonClientImpl = amazonClientImpl;
    }

    @Override
    public List<CapturarProdutoDTO> buscar(String query) {
        Document resultados = amazonClientImpl.getResultados(query);

        return extrair(resultados);
    }

    public List<CapturarProdutoDTO> extrair(Document document) {
        List<CapturarProdutoDTO> listaCapturarProdutosDTO = new ArrayList<>();

        Elements items = document.select(".s-result-item");
        items.remove(0);

        for(Element item : items) {
            CapturarProdutoDTO capturarProdutoDTO = CapturarProdutoMapper.toDTO(item);

            if(capturarProdutoDTO == null) continue;

            listaCapturarProdutosDTO.add(capturarProdutoDTO);
        }

        return listaCapturarProdutosDTO;
    }
}
