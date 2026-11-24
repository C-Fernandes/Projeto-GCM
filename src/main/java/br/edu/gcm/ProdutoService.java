package br.edu.gcm;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.stereotype.Service;

@Service
public class ProdutoService {
    private final ConcurrentMap<Integer, Produto> produtos = new ConcurrentHashMap<>();

    public Optional<Produto> cadastrar(Integer codigo, BigDecimal preco) {
        Produto produto = new Produto(codigo, preco);
        return produtos.putIfAbsent(codigo, produto) == null ? Optional.of(produto) : Optional.empty();
    }

    public Optional<Produto> buscar(Integer codigo) {
        return Optional.ofNullable(produtos.get(codigo));
    }
}
