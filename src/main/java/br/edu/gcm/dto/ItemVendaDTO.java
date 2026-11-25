package br.edu.gcm.dto;

import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Item vendido")
public record ItemVendaDTO(@Schema(example = "123") Integer codigo,
                           @Schema(example = "2") Integer quantidade,
                           @Schema(example = "119.80") BigDecimal subtotal,
                           @Schema(description = "Estoque do produto depois deste item", example = "13") Integer estoque) {}
