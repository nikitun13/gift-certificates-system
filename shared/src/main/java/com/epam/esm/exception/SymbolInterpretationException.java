package com.epam.esm.exception;

public class SymbolInterpretationException extends RuntimeException {

    public SymbolInterpretationException() {
    }

    public SymbolInterpretationException(String message) {
        super(message);
    }

    public SymbolInterpretationException(String message, Throwable cause) {
        super(message, cause);
    }

    public SymbolInterpretationException(Throwable cause) {
        super(cause);
    }
}
