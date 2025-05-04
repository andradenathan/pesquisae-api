package net.pesquisae.adapters.controllers;

import net.pesquisae.domain.usecases.BuscarProdutosUseCase;
import net.pesquisae.domain.usecases.dto.BuscarProdutosResultadoDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {
    private final BuscarProdutosUseCase buscarProdutosUseCase;

    public ProdutoController(BuscarProdutosUseCase buscarProdutosUseCase) {
        this.buscarProdutosUseCase = buscarProdutosUseCase;
    }

    @GetMapping("/buscar")
    public ResponseEntity<BuscarProdutosResultadoDTO> buscarProdutos(
            @RequestParam("q") String query,
            @RequestParam("pagina") Integer pagina)  {
        return ResponseEntity.status(HttpStatus.OK).body(buscarProdutosUseCase.buscar(query, pagina));
    }
}
