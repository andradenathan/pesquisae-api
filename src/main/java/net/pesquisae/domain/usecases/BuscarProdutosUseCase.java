package net.pesquisae.domain.usecases;

import net.pesquisae.adapters.repositories.ProdutoRepository;
import net.pesquisae.adapters.scraper.CapturadorProduto;
import net.pesquisae.domain.usecases.dto.CapturarProdutoDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BuscarProdutosUseCase {
    private final ProdutoRepository produtoRepository;
    private final List<CapturadorProduto> capturadores;

    public BuscarProdutosUseCase(ProdutoRepository produtoRepository, List<CapturadorProduto> capturadores) {
        this.produtoRepository = produtoRepository;
        this.capturadores = capturadores;
    }

    public List<CapturarProdutoDTO> buscar(String query) {
        List<CapturarProdutoDTO> resultados = new ArrayList<>();

        for(CapturadorProduto capturador : capturadores) {
            List<CapturarProdutoDTO> produtos = capturador.buscar(query);

            if(produtos == null) continue;

            resultados.addAll(produtos);
        }

        return resultados;
    }
}
