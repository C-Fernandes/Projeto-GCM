package br.edu.gcm.controller;

import br.edu.gcm.service.ProdutoService;
import br.edu.gcm.mapper.VendaMapper;
import br.edu.gcm.dto.VendaCadastroDTO;
import br.edu.gcm.dto.VendaDTO;
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
@RequestMapping("/loja/venda")
public class VendaController {
    private final ProdutoService service;
    private final VendaMapper mapper;

    public VendaController(ProdutoService service, VendaMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Operation(summary = "Registrar venda", description = "Dá saída no estoque de cada item e calcula os subtotais e o total. Se algum item der erro, nenhum estoque muda.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Venda registrada", content = @Content(schema = @Schema(implementation = VendaDTO.class))),
        @ApiResponse(responseCode = "400", description = "Lista de itens ausente ou vazia, item inválido ou estoque insuficiente", content = @Content(schema = @Schema(implementation = ErroDTO.class))),
        @ApiResponse(responseCode = "404", description = "Algum produto da lista não existe", content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    @PostMapping
    public ResponseEntity<VendaDTO> vender(@Valid @RequestBody VendaCadastroDTO dados) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.paraDTO(service.vender(mapper.paraPedidos(dados))));
    }
}
