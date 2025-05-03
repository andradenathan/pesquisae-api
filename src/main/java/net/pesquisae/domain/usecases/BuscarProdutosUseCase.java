package net.pesquisae.domain.usecases;

import net.pesquisae.adapters.scraper.CapturadorProduto;
import net.pesquisae.domain.usecases.dto.BuscarProdutosResultadoDTO;
import net.pesquisae.domain.usecases.dto.CapturarProdutoDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class BuscarProdutosUseCase {
    private final List<CapturadorProduto> capturadores;
    private final Executor asyncExecutor;

    private static final Logger logger = LoggerFactory.getLogger(BuscarProdutosUseCase.class);

    public BuscarProdutosUseCase(
            List<CapturadorProduto> capturadores,
            @Qualifier("asyncExecutor") Executor asyncExecutor) {
        this.capturadores = capturadores;
        this.asyncExecutor = asyncExecutor;
    }

    public BuscarProdutosResultadoDTO buscar(String query, Integer pagina) {
        List<CompletableFuture<List<CapturarProdutoDTO>>> tarefas = capturadores.stream()
                .map(capturador -> calcularResultadosAsync(capturador, query, pagina))
                .toList();

        List<CapturarProdutoDTO> resultados = tarefas.stream()
                .map(CompletableFuture::join)
                .flatMap(List::stream)
                .sorted(Comparator.comparing(CapturarProdutoDTO::preco))
                .toList();

        return new BuscarProdutosResultadoDTO(resultados, resultados.size());
    }

    private CompletableFuture<List<CapturarProdutoDTO>> calcularResultadosAsync(
            CapturadorProduto capturador,
            String query,
            Integer pagina) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return capturador.buscar(query, pagina);
            } catch (Exception e) {
                logger.warn("Erro no capturador {}: {}", capturador.getClass().getSimpleName(), e.getMessage());
                return Collections.<CapturarProdutoDTO>emptyList();
            }
        }, asyncExecutor).completeOnTimeout(Collections.emptyList(), 10, TimeUnit.SECONDS);
    }
}
