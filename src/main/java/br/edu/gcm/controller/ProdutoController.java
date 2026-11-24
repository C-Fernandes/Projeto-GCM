package br.edu.gcm.controller;

import br.edu.gcm.service.ProdutoService;
import br.edu.gcm.mapper.ProdutoMapper;
import br.edu.gcm.dto.ProdutoCadastroDTO;
import br.edu.gcm.dto.ProdutoDTO;
import br.edu.gcm.dto.EstoqueDTO;
import br.edu.gcm.dto.ErroDTO;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/loja/produto")
public class ProdutoController {
    private final ProdutoService service;
    private final ProdutoMapper mapper;

    public ProdutoController(ProdutoService service, ProdutoMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Operation(summary = "Cadastrar produto", description = "Cria um produto com estoque inicial igual a zero.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Produto criado", content = @Content(schema = @Schema(implementation = ProdutoDTO.class))),
        @ApiResponse(responseCode = "400", description = "Campo obrigatório ausente ou tipo inválido", content = @Content(schema = @Schema(implementation = ErroDTO.class))),
        @ApiResponse(responseCode = "409", description = "Código já cadastrado", content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    @PostMapping
    public ResponseEntity<ProdutoDTO> cadastrar(@Valid @RequestBody ProdutoCadastroDTO dados) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.paraDTO(service.cadastrar(mapper.paraEntidade(dados))));
    }

    @Operation(summary = "Consultar estoque", description = "Consulta a quantidade em estoque pelo código do produto.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Estoque consultado", content = @Content(schema = @Schema(implementation = EstoqueDTO.class))),
        @ApiResponse(responseCode = "400", description = "Código inválido", content = @Content(schema = @Schema(implementation = ErroDTO.class))),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    @GetMapping("/{codigo}/estoque")
    public EstoqueDTO consultarEstoque(@PathVariable Integer codigo) {
        return mapper.paraEstoqueDTO(service.buscar(codigo));
    }
}
