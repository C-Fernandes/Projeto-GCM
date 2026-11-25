package br.edu.gcm.model;

import java.math.BigDecimal;

public record ItemVenda(Produto produto, Integer quantidade) {
    public BigDecimal subtotal() {
        return produto.getPreco().multiply(BigDecimal.valueOf(quantidade));
    }
}
