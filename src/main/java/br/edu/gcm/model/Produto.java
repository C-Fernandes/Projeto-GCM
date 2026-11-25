package br.edu.gcm.model;

import br.edu.gcm.exception.DadosInvalidosException;
import br.edu.gcm.exception.EstoqueInsuficienteException;
import java.math.BigDecimal;

public class Produto {
    private final Integer codigo;
    private final BigDecimal preco;
    private final Integer estoque;

    public Produto(Integer codigo, BigDecimal preco) {
        this(codigo, preco, 0);
        if (codigo == null) throw new DadosInvalidosException("Informe o código do produto.");
        if (preco == null || preco.signum() < 0) throw new DadosInvalidosException("Informe um preço maior ou igual a zero.");
    }

    private Produto(Integer codigo, BigDecimal preco, Integer estoque) {
        this.codigo = codigo;
        this.preco = preco;
        this.estoque = estoque;
    }

    public Produto comEntrada(Integer quantidade) {
        validarQuantidade(quantidade);
        try {
            return new Produto(codigo, preco, Math.addExact(estoque, quantidade));
        } catch (ArithmeticException e) {
            throw new DadosInvalidosException("Quantidade excede o limite do estoque.");
        }
    }

    public Produto comSaida(Integer quantidade) {
        validarQuantidade(quantidade);
        if (quantidade > estoque) throw new EstoqueInsuficienteException();
        return new Produto(codigo, preco, estoque - quantidade);
    }

    private static void validarQuantidade(Integer quantidade) {
        if (quantidade == null || quantidade <= 0) throw new DadosInvalidosException("Informe uma quantidade inteira maior que zero.");
    }

    public Integer getCodigo() { return codigo; }
    public BigDecimal getPreco() { return preco; }
    public Integer getEstoque() { return estoque; }
}
