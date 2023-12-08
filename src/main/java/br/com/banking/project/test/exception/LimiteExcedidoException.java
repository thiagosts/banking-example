package br.com.banking.project.test.exception;

public class LimiteExcedidoException extends RuntimeException {

    public LimiteExcedidoException() {
        super();
    }

    public LimiteExcedidoException(String message) {
        super(message);
    }

    public LimiteExcedidoException(String message, Throwable cause) {
        super(message, cause);
    }

    public LimiteExcedidoException(Throwable cause) {
        super(cause);
    }
}

