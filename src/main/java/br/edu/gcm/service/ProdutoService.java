package br.edu.gcm.service;

import br.edu.gcm.model.Produto;
import br.edu.gcm.exception.ProdutoDuplicadoException;
import br.edu.gcm.exception.ProdutoNaoEncontradoException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.stereotype.Service;

@Service
public class ProdutoService {
    private final ConcurrentMap<Integer, Produto> produtos = new ConcurrentHashMap<>();

    public Produto cadastrar(Produto produto) {
        if (produtos.putIfAbsent(produto.getCodigo(), produto) != null) {
            throw new ProdutoDuplicadoException();
        }
        return produto;
    }

    public Produto buscar(Integer codigo) {
        Produto produto = produtos.get(codigo);
        if (produto == null) throw new ProdutoNaoEncontradoException();
        return produto;
    }

    public Produto entrada(Integer codigo, int quantidade) {
        Produto produto = produtos.computeIfPresent(codigo, (chave, atual) -> atual.comEntrada(quantidade));
        if (produto == null) throw new ProdutoNaoEncontradoException();
        return produto;
    }
}
