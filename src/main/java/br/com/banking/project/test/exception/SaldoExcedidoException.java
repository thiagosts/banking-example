package br.com.banking.project.test.exception;

public class SaldoExcedidoException extends RuntimeException {

    public SaldoExcedidoException() {
        super();
    }

    public SaldoExcedidoException(String message) {
        super(message);
    }

    public SaldoExcedidoException(String message, Throwable cause) {
        super(message, cause);
    }

    public SaldoExcedidoException(Throwable cause) {
        super(cause);
    }
}

