package com.join.Mcqueen.exceptions;

/**
 * Exceção lançada quando há problemas no processamento de arquivos
 */
public class FileProcessingException extends RuntimeException {

    public FileProcessingException(String message) {
        super(message);
    }

    public FileProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}