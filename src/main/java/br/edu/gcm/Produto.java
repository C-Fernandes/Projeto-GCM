package br.edu.gcm;

import java.math.BigDecimal;

public record Produto(Integer codigo, BigDecimal preco, Integer estoque) {
    public Produto(Integer codigo, BigDecimal preco) {
        this(codigo, preco, 0);
    }
}
