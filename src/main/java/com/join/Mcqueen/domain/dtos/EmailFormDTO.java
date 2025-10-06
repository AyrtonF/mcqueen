package com.join.Mcqueen.domain.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para receber dados do formulário de envio de email
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dados do formulário para envio de email")
public class EmailFormDTO {

    @NotBlank(message = "Nome do órgão é obrigatório")
    @Size(max = 255, message = "Nome do órgão deve ter no máximo 255 caracteres")
    @Schema(description = "Nome do órgão responsável", example = "Secretaria da Saúde")
    private String organizationName;

    @NotBlank(message = "Contato do responsável é obrigatório")
    @Email(message = "Contato deve ser um email válido")
    @Schema(description = "Email do responsável", example = "seuemail@dominio.com.br")
    private String responsibleContact;

    @NotBlank(message = "Tema é obrigatório")
    @Size(max = 100, message = "Tema deve ter no máximo 100 caracteres")
    @Schema(description = "Tema do formulário", example = "Saúde Pública")
    private String subject;

    @NotBlank(message = "Período de referência é obrigatório")
    @Size(max = 50, message = "Período de referência deve ter no máximo 50 caracteres")
    @Schema(description = "Período de referência dos dados", example = "2022-2024")
    private String referencePeriod;

    @NotBlank(message = "Descrição do dado é obrigatória")
    @Size(max = 2000, message = "Descrição deve ter no máximo 2000 caracteres")
    @Schema(description = "Descrição detalhada do conteúdo dos arquivos")
    private String dataDescription;

    @Schema(description = "Confirmação de conformidade com LGPD", example = "true")
    private Boolean lgpdCompliance;
}