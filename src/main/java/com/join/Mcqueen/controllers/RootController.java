package com.join.Mcqueen.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class RootController {

    /**
     * Endpoint raiz - redireciona para documentação Swagger
     */
    @GetMapping("")
    @Operation(summary = "Página inicial", description = "Redireciona para a documentação da API")
    @ApiResponse(responseCode = "302", description = "Redirecionamento para Swagger UI")
    public ResponseEntity<Void> home() {
        return ResponseEntity.status(302)
                .header("Location", "/swagger-ui/index.html")
                .build();
    }
}
