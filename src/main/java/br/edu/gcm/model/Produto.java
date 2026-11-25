package br.edu.gcm.model;

import java.math.BigDecimal;

public class Produto {
    private final Integer codigo;
    private final BigDecimal preco;
    private final Integer estoque;

    public Produto(Integer codigo, BigDecimal preco) {
        this(codigo, preco, 0);
    }

    private Produto(Integer codigo, BigDecimal preco, Integer estoque) {
        this.codigo = codigo;
        this.preco = preco;
        this.estoque = estoque;
    }

    public Produto comEntrada(int quantidade) {
        return new Produto(codigo, preco, Math.addExact(estoque, quantidade));
    }

    public Integer getCodigo() { return codigo; }
    public BigDecimal getPreco() { return preco; }
    public Integer getEstoque() { return estoque; }
}
