package br.edu.gcm.dto;

import java.math.BigDecimal;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Venda registrada")
public record VendaDTO(@Schema(example = "189.70") BigDecimal total,
                       List<ItemVendaDTO> itens) {}
