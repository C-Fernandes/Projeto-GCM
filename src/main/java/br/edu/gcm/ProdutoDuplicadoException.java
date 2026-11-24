package br.edu.gcm;

public class ProdutoDuplicadoException extends RuntimeException {
    public ProdutoDuplicadoException() { super("J? existe um produto com este c?digo."); }
}
