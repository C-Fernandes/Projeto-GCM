package br.edu.gcm.exception;

public class ProdutoDuplicadoException extends RuntimeException {
    public ProdutoDuplicadoException() { super("Já existe um produto com este código."); }
}
