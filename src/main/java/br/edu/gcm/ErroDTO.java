package br.edu.gcm;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Erro da requisi??o")
public record ErroDTO(@Schema(example = "400") int status,
                      @Schema(example = "Requisi??o inv?lida.") String mensagem) {}
