package com.join.Mcqueen.domain.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO de resposta para operações de email
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Resposta do envio de email")
public class EmailResponseDTO {

    @Schema(description = "Status do envio", example = "SUCCESS")
    private String status;

    @Schema(description = "Mensagem de retorno", example = "Email enviado com sucesso")
    private String message;

    @Schema(description = "Data e hora do envio")
    private LocalDateTime sendDate;

    @Schema(description = "Email de destino", example = "destinatario@exemplo.com")
    private String recipient;

    @Schema(description = "Quantidade de arquivos anexados", example = "3")
    private Integer fileCount;

    @Schema(description = "Lista de nomes dos arquivos anexados")
    private List<String> fileNames;
}