package br.edu.gcm.exception;

public class EstoqueInsuficienteException extends RuntimeException {
    public EstoqueInsuficienteException() { super("Estoque insuficiente para a saída."); }
}
