package br.edu.gcm.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Erro da requisição")
public record ErroDTO(@Schema(example = "400") int status,
                      @Schema(example = "Requisição inválida.") String mensagem) {}
