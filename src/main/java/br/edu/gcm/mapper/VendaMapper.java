package br.edu.gcm.mapper;

import br.edu.gcm.model.ItemPedido;
import br.edu.gcm.model.ItemVenda;
import br.edu.gcm.model.Venda;
import br.edu.gcm.dto.VendaCadastroDTO;
import br.edu.gcm.dto.VendaDTO;
import br.edu.gcm.dto.ItemVendaDTO;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class VendaMapper {
    public List<ItemPedido> paraPedidos(VendaCadastroDTO dto) {
        return dto.itens().stream().map(item -> new ItemPedido(item.codigo(), item.quantidade())).toList();
    }
    public VendaDTO paraDTO(Venda venda) {
        return new VendaDTO(venda.total(), venda.itens().stream().map(this::paraItemDTO).toList());
    }
    private ItemVendaDTO paraItemDTO(ItemVenda item) {
        return new ItemVendaDTO(item.produto().getCodigo(), item.quantidade(), item.subtotal(), item.produto().getEstoque());
    }
}
