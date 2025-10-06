package com.join.Mcqueen.exceptions;

/**
 * Exceção lançada quando há problemas no envio de email
 */
public class EmailSendingException extends RuntimeException {

    public EmailSendingException(String message) {
        super(message);
    }

    public EmailSendingException(String message, Throwable cause) {
        super(message, cause);
    }
}