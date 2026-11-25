package br.edu.gcm.service;

import br.edu.gcm.model.ItemPedido;
import br.edu.gcm.model.ItemVenda;
import br.edu.gcm.model.Produto;
import br.edu.gcm.model.Venda;
import br.edu.gcm.exception.ProdutoDuplicadoException;
import br.edu.gcm.exception.ProdutoNaoEncontradoException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    public synchronized Produto entrada(Integer codigo, int quantidade) {
        Produto produto = produtos.computeIfPresent(codigo, (chave, atual) -> atual.comEntrada(quantidade));
        if (produto == null) throw new ProdutoNaoEncontradoException();
        return produto;
    }

    public synchronized Produto saida(Integer codigo, int quantidade) {
        Produto produto = produtos.computeIfPresent(codigo, (chave, atual) -> atual.comSaida(quantidade));
        if (produto == null) throw new ProdutoNaoEncontradoException();
        return produto;
    }

    public synchronized Venda vender(List<ItemPedido> pedidos) {
        Map<Integer, Produto> atualizados = new HashMap<>();
        List<ItemVenda> itens = new ArrayList<>();
        for (ItemPedido pedido : pedidos) {
            Produto atual = atualizados.containsKey(pedido.codigo()) ? atualizados.get(pedido.codigo()) : buscar(pedido.codigo());
            Produto novo = atual.comSaida(pedido.quantidade());
            atualizados.put(pedido.codigo(), novo);
            itens.add(new ItemVenda(novo, pedido.quantidade()));
        }
        produtos.putAll(atualizados);
        return new Venda(itens);
    }
}
