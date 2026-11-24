package br.edu.gcm.mapper;

import br.edu.gcm.model.Produto;
import br.edu.gcm.dto.ProdutoCadastroDTO;
import br.edu.gcm.dto.ProdutoDTO;
import br.edu.gcm.dto.EstoqueDTO;
import org.springframework.stereotype.Component;

@Component
public class ProdutoMapper {
    public Produto paraEntidade(ProdutoCadastroDTO dto) {
        return new Produto(dto.codigo(), dto.preco());
    }
    public ProdutoDTO paraDTO(Produto produto) {
        return new ProdutoDTO(produto.getCodigo(), produto.getPreco(), produto.getEstoque());
    }
    public EstoqueDTO paraEstoqueDTO(Produto produto) {
        return new EstoqueDTO(produto.getCodigo(), produto.getEstoque());
    }
}
