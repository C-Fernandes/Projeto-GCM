package br.edu.gcm.model;

import java.math.BigDecimal;

public class Produto {
    private final Integer codigo;
    private final BigDecimal preco;
    private final Integer estoque;

    public Produto(Integer codigo, BigDecimal preco) {
        this.codigo = codigo;
        this.preco = preco;
        this.estoque = 0;
    }

    public Integer getCodigo() { return codigo; }
    public BigDecimal getPreco() { return preco; }
    public Integer getEstoque() { return estoque; }
}
