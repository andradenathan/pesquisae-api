package net.pesquisae.adapters.scraper;

import net.pesquisae.domain.usecases.dto.CapturarProdutoDTO;
import net.pesquisae.infra.external.mercadolivre.MercadoLivreClientImpl;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CapturadorMercadoLivreAdapter implements CapturadorProduto {
    private final MercadoLivreClientImpl mercadoLivreClientImpl;

    public CapturadorMercadoLivreAdapter(MercadoLivreClientImpl mercadoLivreClientImpl) {
        this.mercadoLivreClientImpl = mercadoLivreClientImpl;
    }

    @Override
    public List<CapturarProdutoDTO> buscar(String query) {
        Document resultados = mercadoLivreClientImpl.getResultados(query);

        return extrair(resultados);
    }

    @Override
    public List<CapturarProdutoDTO> buscarAsync(String query) {
        Document resultados = mercadoLivreClientImpl.getResultados(query);

        // TODO: Utilizar estratégia de produtor e consumidores.

        return null;
    }

    @Override
    public List<CapturarProdutoDTO> extrair(Document document) {
        return null;
    }
}
