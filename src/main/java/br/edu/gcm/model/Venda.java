package br.edu.gcm.model;

import java.math.BigDecimal;
import java.util.List;

public record Venda(List<ItemVenda> itens) {
    public BigDecimal total() {
        return itens.stream().map(ItemVenda::subtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
