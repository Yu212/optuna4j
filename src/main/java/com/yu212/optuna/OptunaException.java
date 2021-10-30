package com.yu212.optuna;

public class OptunaException extends Exception {
    public OptunaException() {
        super();
    }

    public OptunaException(String message) {
        super(message);
    }

    public OptunaException(String message, Throwable cause) {
        super(message, cause);
    }

    public OptunaException(Throwable cause) {
        super(cause);
    }
}
