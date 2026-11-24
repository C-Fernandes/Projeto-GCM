package br.edu.gcm;

import java.math.BigDecimal;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para cadastro de produto")
public record ProdutoCadastroDTO(
        @NotNull @Schema(description = "C?digo inteiro do produto", example = "123", requiredMode = Schema.RequiredMode.REQUIRED) Integer codigo,
        @NotNull @Schema(description = "Pre?o decimal do produto", example = "59.90", requiredMode = Schema.RequiredMode.REQUIRED) BigDecimal preco) {}
