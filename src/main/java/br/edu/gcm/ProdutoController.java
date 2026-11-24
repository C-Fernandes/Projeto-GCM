package br.edu.gcm;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/loja/produto")
public class ProdutoController {
    private final ProdutoService service;

    public ProdutoController(ProdutoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Produto> cadastrar(@RequestBody JsonNode body) {
        JsonNode codigo = body.path("codigo");
        JsonNode preco = body.path("preco");
        if (!body.isObject() || !codigo.isIntegralNumber() || !codigo.canConvertToInt() || !preco.isNumber()) {
            return ResponseEntity.badRequest().build();
        }
        return service.cadastrar(codigo.intValue(), preco.decimalValue())
                .map(produto -> ResponseEntity.status(HttpStatus.CREATED).body(produto))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.CONFLICT).build());
    }

    @GetMapping("/{codigo}/estoque")
    public ResponseEntity<Map<String, Integer>> consultarEstoque(@PathVariable Integer codigo) {
        return service.buscar(codigo)
                .map(produto -> ResponseEntity.ok(Map.of("codigo", produto.codigo(), "estoque", produto.estoque())))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
