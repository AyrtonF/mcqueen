package com.join.Mcqueen.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Classe para padronizar respostas de erro da API
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {

    private int status;
    private String error;
    private String message;
    private String path;
    private LocalDateTime timestamp;
    private List<String> details;

    public static ErrorResponse builder() {
        return new ErrorResponse();
    }

    public ErrorResponse status(int status) {
        this.status = status;
        return this;
    }

    public ErrorResponse error(String error) {
        this.error = error;
        return this;
    }

    public ErrorResponse message(String message) {
        this.message = message;
        return this;
    }

    public ErrorResponse path(String path) {
        this.path = path;
        return this;
    }

    public ErrorResponse timestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public ErrorResponse details(List<String> details) {
        this.details = details;
        return this;
    }

    public ErrorResponse build() {
        if (this.timestamp == null) {
            this.timestamp = LocalDateTime.now();
        }
        return this;
    }
}