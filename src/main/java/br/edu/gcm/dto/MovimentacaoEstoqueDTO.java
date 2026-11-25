package br.edu.gcm.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para movimentação de estoque")
public record MovimentacaoEstoqueDTO(
        @NotNull @Positive @Schema(description = "Quantidade inteira maior que zero", example = "10", requiredMode = Schema.RequiredMode.REQUIRED) Integer quantidade) {}
