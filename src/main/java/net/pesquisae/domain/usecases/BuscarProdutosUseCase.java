package net.pesquisae.domain.usecases;

import net.pesquisae.adapters.scraper.CapturadorProduto;
import net.pesquisae.domain.usecases.dto.BuscarProdutosResultadoDTO;
import net.pesquisae.domain.usecases.dto.CapturarProdutoDTO;
import net.pesquisae.domain.usecases.dto.ResultadoPaginaCapturador;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
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
        List<CompletableFuture<ResultadoPaginaCapturador>> tarefas = capturadores.stream()
                .map(capturador -> calcularResultadosAsync(capturador, query, pagina))
                .toList();

        List<ResultadoPaginaCapturador> resultadosCompletos = tarefas.stream()
                .map(CompletableFuture::join)
                .toList();

        List<CapturarProdutoDTO> produtosAgregados = resultadosCompletos.stream()
                .map(ResultadoPaginaCapturador::produtos)
                .flatMap(List::stream)
                .sorted(Comparator.comparing(CapturarProdutoDTO::preco))
                .toList();

        int maxTotalPaginas = resultadosCompletos.stream()
                .mapToInt(ResultadoPaginaCapturador::totalPaginas)
                .max()
                .orElse(1);

        return new BuscarProdutosResultadoDTO(produtosAgregados, produtosAgregados.size(), maxTotalPaginas);
    }

    private CompletableFuture<ResultadoPaginaCapturador> calcularResultadosAsync(
            CapturadorProduto capturador,
            String query,
            Integer pagina) {
        final ResultadoPaginaCapturador defaultResultOnErrorOrTimeout =
                new ResultadoPaginaCapturador(Collections.emptyList(), 0);

        return CompletableFuture.supplyAsync(() -> {
            try {
                return capturador.buscar(query, pagina);
            } catch (IOException e) {
                throw new RuntimeException("Falha ao buscar em " + capturador.getClass().getSimpleName(), e);
            }
        }, asyncExecutor)
                .exceptionally(ex -> {
                    Throwable cause = (ex instanceof RuntimeException && ex.getCause() != null) ? ex.getCause() : ex;
                    logger.warn("Erro assíncrono no capturador {} ao buscar query '{}' pagina {}: {}",
                            capturador.getClass().getSimpleName(), query, pagina, cause.getMessage(), cause);

                    return defaultResultOnErrorOrTimeout;
                })
                .completeOnTimeout(defaultResultOnErrorOrTimeout, 10, TimeUnit.SECONDS);
    }
}
