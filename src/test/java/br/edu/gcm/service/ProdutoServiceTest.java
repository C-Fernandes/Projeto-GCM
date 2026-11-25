package br.edu.gcm.service;

import static org.junit.jupiter.api.Assertions.*;

import br.edu.gcm.exception.DadosInvalidosException;
import br.edu.gcm.exception.EstoqueInsuficienteException;
import br.edu.gcm.exception.ProdutoDuplicadoException;
import br.edu.gcm.exception.ProdutoNaoEncontradoException;
import br.edu.gcm.model.ItemPedido;
import br.edu.gcm.model.Produto;
import br.edu.gcm.model.Venda;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Testes unitários da camada de serviço, sem passar pela API REST (Issue #7). */
class ProdutoServiceTest {
    private ProdutoService service;

    @BeforeEach
    void setUp() {
        service = new ProdutoService();
    }

    private Produto cadastrar(int codigo, String preco, int estoque) {
        service.cadastrar(new Produto(codigo, new BigDecimal(preco)));
        return estoque > 0 ? service.entrada(codigo, estoque) : service.buscar(codigo);
    }

    // Cadastrar Produto

    @Test
    void cadastraProdutoComEstoqueZero() {
        Produto produto = service.cadastrar(new Produto(123, new BigDecimal("59.90")));
        assertEquals(123, produto.getCodigo());
        assertEquals(new BigDecimal("59.90"), produto.getPreco());
        assertEquals(0, produto.getEstoque());
    }

    @Test
    void cadastroDuplicadoLancaExcecaoEPreservaOriginal() {
        cadastrar(1, "10.00", 4);
        assertThrows(ProdutoDuplicadoException.class, () -> service.cadastrar(new Produto(1, new BigDecimal("99"))));
        assertEquals(4, service.buscar(1).getEstoque());
        assertEquals(new BigDecimal("10.00"), service.buscar(1).getPreco());
    }

    @Test
    void cadastroRejeitaDadosInvalidos() {
        assertThrows(DadosInvalidosException.class, () -> new Produto(null, BigDecimal.ONE));
        assertThrows(DadosInvalidosException.class, () -> new Produto(1, null));
        assertThrows(DadosInvalidosException.class, () -> new Produto(1, new BigDecimal("-0.01")));
        assertThrows(DadosInvalidosException.class, () -> service.cadastrar(null));
    }

    // Consultar Estoque

    @Test
    void consultaProdutoInexistenteLancaExcecao() {
        assertThrows(ProdutoNaoEncontradoException.class, () -> service.buscar(999));
        assertThrows(DadosInvalidosException.class, () -> service.buscar(null));
    }

    // Entrada de Estoque

    @Test
    void entradaSomaAoEstoque() {
        cadastrar(1, "1", 0);
        assertEquals(10, service.entrada(1, 10).getEstoque());
        assertEquals(25, service.entrada(1, 15).getEstoque());
        assertEquals(25, service.buscar(1).getEstoque());
    }

    @Test
    void entradaRejeitaQuantidadeInvalidaSemAlterarEstoque() {
        cadastrar(1, "1", 5);
        for (Integer quantidade : Arrays.asList(null, 0, -1)) {
            assertThrows(DadosInvalidosException.class, () -> service.entrada(1, quantidade));
        }
        assertThrows(DadosInvalidosException.class, () -> service.entrada(1, Integer.MAX_VALUE));
        assertEquals(5, service.buscar(1).getEstoque());
    }

    @Test
    void entradaEmProdutoInexistenteLancaExcecao() {
        assertThrows(ProdutoNaoEncontradoException.class, () -> service.entrada(999, 1));
    }

    // Saída de Estoque

    @Test
    void saidaSubtraiDoEstoque() {
        cadastrar(1, "1", 15);
        assertEquals(5, service.saida(1, 10).getEstoque());
        assertEquals(0, service.saida(1, 5).getEstoque());
    }

    @Test
    void saidaRejeitaQuantidadeInvalidaOuMaiorQueEstoqueSemAlterarEstoque() {
        cadastrar(1, "1", 5);
        for (Integer quantidade : Arrays.asList(null, 0, -1)) {
            assertThrows(DadosInvalidosException.class, () -> service.saida(1, quantidade));
        }
        assertThrows(EstoqueInsuficienteException.class, () -> service.saida(1, 6));
        assertEquals(5, service.buscar(1).getEstoque());
    }

    @Test
    void saidaEmProdutoInexistenteLancaExcecao() {
        assertThrows(ProdutoNaoEncontradoException.class, () -> service.saida(999, 1));
    }

    // Venda

    @Test
    void vendaCalculaSubtotaisTotalEBaixaEstoque() {
        cadastrar(123, "59.90", 15);
        cadastrar(456, "69.90", 8);
        Venda venda = service.vender(List.of(new ItemPedido(123, 2), new ItemPedido(456, 1)));
        assertEquals(0, new BigDecimal("189.70").compareTo(venda.total()));
        assertEquals(0, new BigDecimal("119.80").compareTo(venda.itens().get(0).subtotal()));
        assertEquals(0, new BigDecimal("69.90").compareTo(venda.itens().get(1).subtotal()));
        assertEquals(13, service.buscar(123).getEstoque());
        assertEquals(7, service.buscar(456).getEstoque());
    }

    @Test
    void vendaComProdutoInexistenteNaoAlteraNenhumEstoque() {
        cadastrar(1, "10", 5);
        assertThrows(ProdutoNaoEncontradoException.class,
                () -> service.vender(List.of(new ItemPedido(1, 2), new ItemPedido(999, 1))));
        assertEquals(5, service.buscar(1).getEstoque());
    }

    @Test
    void vendaComEstoqueInsuficienteNoUltimoItemNaoAlteraNenhumEstoque() {
        cadastrar(1, "10", 5);
        cadastrar(2, "10", 5);
        cadastrar(3, "10", 1);
        assertThrows(EstoqueInsuficienteException.class,
                () -> service.vender(List.of(new ItemPedido(1, 5), new ItemPedido(2, 5), new ItemPedido(3, 2))));
        assertEquals(5, service.buscar(1).getEstoque());
        assertEquals(5, service.buscar(2).getEstoque());
        assertEquals(1, service.buscar(3).getEstoque());
    }

    @Test
    void vendaComItemInvalidoNaoAlteraNenhumEstoque() {
        cadastrar(1, "10", 5);
        assertThrows(DadosInvalidosException.class, () -> service.vender(List.of(new ItemPedido(1, 1), new ItemPedido(1, 0))));
        assertThrows(DadosInvalidosException.class, () -> service.vender(List.of(new ItemPedido(1, 1), new ItemPedido(null, 1))));
        assertThrows(DadosInvalidosException.class, () -> service.vender(Arrays.asList(new ItemPedido(1, 1), null)));
        assertEquals(5, service.buscar(1).getEstoque());
    }

    @Test
    void vendaRejeitaListaAusenteOuVazia() {
        assertThrows(DadosInvalidosException.class, () -> service.vender(null));
        assertThrows(DadosInvalidosException.class, () -> service.vender(List.of()));
    }

    @Test
    void vendaConsideraItensRepetidosDoMesmoProduto() {
        cadastrar(1, "2.50", 5);
        assertThrows(EstoqueInsuficienteException.class, () -> service.vender(List.of(new ItemPedido(1, 3), new ItemPedido(1, 3))));
        assertEquals(5, service.buscar(1).getEstoque());
        Venda venda = service.vender(List.of(new ItemPedido(1, 2), new ItemPedido(1, 3)));
        assertEquals(0, new BigDecimal("12.50").compareTo(venda.total()));
        assertEquals(0, service.buscar(1).getEstoque());
    }

    // Fluxo completo das cinco operações

    @Test
    void fluxoCompletoDasCincoOperacoes() {
        service.cadastrar(new Produto(10, new BigDecimal("5.00")));
        assertEquals(0, service.buscar(10).getEstoque());
        service.entrada(10, 20);
        service.saida(10, 3);
        assertEquals(17, service.buscar(10).getEstoque());
        Venda venda = service.vender(List.of(new ItemPedido(10, 7)));
        assertEquals(0, new BigDecimal("35.00").compareTo(venda.total()));
        assertEquals(10, service.buscar(10).getEstoque());
    }
}
