package net.pesquisae.domain.usecases;

import net.pesquisae.adapters.repositories.ProdutoRepository;
import org.springframework.stereotype.Service;

@Service
public class BuscarProdutosUseCase {
    private final ProdutoRepository produtoRepository;

    public BuscarProdutosUseCase(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public void execute(String query) {}
}
