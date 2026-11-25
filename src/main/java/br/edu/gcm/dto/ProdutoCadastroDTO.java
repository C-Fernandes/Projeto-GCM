package br.edu.gcm.dto;

import java.math.BigDecimal;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para cadastro de produto")
public record ProdutoCadastroDTO(
        @NotNull @Schema(description = "Código inteiro do produto", example = "123", requiredMode = Schema.RequiredMode.REQUIRED) Integer codigo,
        @NotNull @Schema(description = "Preço decimal do produto", example = "59.90", requiredMode = Schema.RequiredMode.REQUIRED) BigDecimal preco) {}
