package br.edu.gcm.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Item a vender")
public record ItemVendaCadastroDTO(
        @NotNull @Schema(description = "Código inteiro do produto", example = "123", requiredMode = Schema.RequiredMode.REQUIRED) Integer codigo,
        @NotNull @Positive @Schema(description = "Quantidade inteira maior que zero", example = "2", requiredMode = Schema.RequiredMode.REQUIRED) Integer quantidade) {}
