package br.edu.gcm;

import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Produto cadastrado")
public record ProdutoDTO(@Schema(example = "123") Integer codigo,
                         @Schema(example = "59.90") BigDecimal preco,
                         @Schema(example = "0") Integer estoque) {}
