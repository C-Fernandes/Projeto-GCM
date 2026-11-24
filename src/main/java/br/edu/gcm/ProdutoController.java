package br.edu.gcm;

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
        @ApiResponse(responseCode = "400", description = "Campo obrigat?rio ausente ou tipo inv?lido", content = @Content(schema = @Schema(implementation = ErroDTO.class))),
        @ApiResponse(responseCode = "409", description = "C?digo j? cadastrado", content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    @PostMapping
    public ResponseEntity<ProdutoDTO> cadastrar(@Valid @RequestBody ProdutoCadastroDTO dados) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.paraDTO(service.cadastrar(mapper.paraEntidade(dados))));
    }

    @Operation(summary = "Consultar estoque", description = "Consulta a quantidade em estoque pelo c?digo do produto.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Estoque consultado", content = @Content(schema = @Schema(implementation = EstoqueDTO.class))),
        @ApiResponse(responseCode = "400", description = "C?digo inv?lido", content = @Content(schema = @Schema(implementation = ErroDTO.class))),
        @ApiResponse(responseCode = "404", description = "Produto n?o encontrado", content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    @GetMapping("/{codigo}/estoque")
    public EstoqueDTO consultarEstoque(@PathVariable Integer codigo) {
        return mapper.paraEstoqueDTO(service.buscar(codigo));
    }
}
