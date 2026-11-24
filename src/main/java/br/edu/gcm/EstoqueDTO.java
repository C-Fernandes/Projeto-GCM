package br.edu.gcm;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Estoque do produto")
public record EstoqueDTO(@Schema(example = "123") Integer codigo,
                         @Schema(example = "0") Integer estoque) {}
