package br.edu.gcm;

public class ProdutoNaoEncontradoException extends RuntimeException {
    public ProdutoNaoEncontradoException() { super("Produto n?o encontrado."); }
}
