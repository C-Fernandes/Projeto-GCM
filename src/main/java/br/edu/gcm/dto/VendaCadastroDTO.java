package br.edu.gcm.dto;

import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para registrar uma venda")
public record VendaCadastroDTO(
        @NotEmpty @ArraySchema(arraySchema = @Schema(description = "Itens vendidos, com pelo menos um item", requiredMode = Schema.RequiredMode.REQUIRED))
        List<@NotNull @Valid ItemVendaCadastroDTO> itens) {}
