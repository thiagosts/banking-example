package br.com.banking.project.test.exception;

public class ContaInativaException extends RuntimeException {

    public ContaInativaException() {
        super();
    }

    public ContaInativaException(String message) {
        super(message);
    }

    public ContaInativaException(String message, Throwable cause) {
        super(message, cause);
    }

    public ContaInativaException(Throwable cause) {
        super(cause);
    }
}

