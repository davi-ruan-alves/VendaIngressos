package venda_ingresso.exceptions;

public class SetorEsgotadoException extends RuntimeException {
    public SetorEsgotadoException(String message) {
        super(message);
    }
}
