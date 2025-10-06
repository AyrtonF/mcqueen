package com.join.Mcqueen.controllers;

import com.join.Mcqueen.domain.dtos.EmailFormDTO;
import com.join.Mcqueen.domain.dtos.EmailResponseDTO;
import com.join.Mcqueen.domain.models.EmailAudit;
import com.join.Mcqueen.services.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controller responsável pelos endpoints de email
 */
@Slf4j
@RestController
@RequestMapping("/api/emails")
@RequiredArgsConstructor
@Tag(name = "Email Controller", description = "Endpoints para envio de emails com formulários e anexos CSV")
public class EmailController {

    private final EmailService emailService;

    /**
     * Endpoint principal para envio de email com formulário e arquivos CSV
     */
    @PostMapping(value = "/send", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Enviar email com formulário e anexos CSV", description = "Envia um email contendo os dados do formulário e arquivos CSV como anexos. "
            +
            "Todos os campos do formulário são obrigatórios e pelo menos um arquivo CSV deve ser anexado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email enviado com sucesso", content = @Content(schema = @Schema(implementation = EmailResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos no formulário ou arquivos"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor ao enviar email")
    })
    public ResponseEntity<EmailResponseDTO> sendEmail(
            @Parameter(description = "Nome do órgão responsável", required = true) @RequestParam("organizationName") String organizationName,

            @Parameter(description = "Email de contato do responsável", required = true) @RequestParam("responsibleContact") String responsibleContact,

            @Parameter(description = "Tema do formulário", required = true) @RequestParam("subject") String subject,

            @Parameter(description = "Período de referência dos dados", required = true) @RequestParam("referencePeriod") String referencePeriod,

            @Parameter(description = "Descrição detalhada do conteúdo dos dados", required = true) @RequestParam("dataDescription") String dataDescription,

            @Parameter(description = "Confirmação de conformidade com LGPD") @RequestParam(value = "lgpdCompliance", defaultValue = "false") Boolean lgpdCompliance,

            @Parameter(description = "Arquivos CSV a serem anexados (um ou mais)", required = true) @RequestParam("files") List<MultipartFile> files,

            @Parameter(description = "Email de destino (opcional, usa padrão se não informado)") @RequestParam(value = "recipient", required = false) String recipient) {
        log.info("Recebida solicitação de envio de email de: {}", responsibleContact);

        EmailFormDTO emailFormDTO = new EmailFormDTO(
                organizationName, responsibleContact, subject, referencePeriod,
                dataDescription, lgpdCompliance);

        EmailResponseDTO response = emailService.sendFormEmail(emailFormDTO, files, recipient);

        log.info("Email enviado com sucesso para: {}", response.getRecipient());
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint alternativo usando JSON + multipart
     */
    @PostMapping(value = "/send-json", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Enviar email usando JSON para dados do formulário", description = "Alternativa para envio usando JSON para os dados do formulário e multipart para arquivos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email enviado com sucesso", content = @Content(schema = @Schema(implementation = EmailResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos no formulário ou arquivos"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor ao enviar email")
    })
    public ResponseEntity<EmailResponseDTO> sendEmailJson(
            @Parameter(description = "Dados do formulário em formato JSON", required = true) @RequestPart("formData") @Valid EmailFormDTO emailFormDTO,

            @Parameter(description = "Arquivos CSV a serem anexados", required = true) @RequestPart("files") List<MultipartFile> files,

            @Parameter(description = "Email de destino (opcional)") @RequestParam(value = "recipient", required = false) String recipient) {
        log.info("Recebida solicitação de envio de email JSON de: {}", emailFormDTO.getResponsibleContact());

        EmailResponseDTO response = emailService.sendFormEmail(emailFormDTO, files, recipient);

        log.info("Email JSON enviado com sucesso para: {}", response.getRecipient());
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint para consultar histórico de emails enviados
     */
    @GetMapping("/history")
    @Operation(summary = "Consultar histórico de emails enviados", description = "Retorna o histórico de emails enviados, opcionalmente filtrado por período")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Histórico recuperado com sucesso", content = @Content(schema = @Schema(implementation = EmailAudit.class)))
    })
    public ResponseEntity<List<EmailAudit>> getHistory(
            @Parameter(description = "Data de início do filtro (formato: yyyy-MM-ddTHH:mm:ss)") @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime startDate,

            @Parameter(description = "Data de fim do filtro (formato: yyyy-MM-ddTHH:mm:ss)") @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime endDate) {
        log.info("Consultando histórico de emails - Período: {} até {}", startDate, endDate);

        List<EmailAudit> history = emailService.getEmailHistory(startDate, endDate);

        return ResponseEntity.ok(history);
    }

    /**
     * Endpoint para consultar emails enviados recentemente (últimas 24h)
     */
    @GetMapping("/recent")
    @Operation(summary = "Consultar emails enviados nas últimas 24 horas", description = "Retorna os emails enviados nas últimas 24 horas")
    @ApiResponse(responseCode = "200", description = "Emails recentes recuperados com sucesso")
    public ResponseEntity<List<EmailAudit>> getRecentEmails() {
        log.info("Consultando emails enviados nas últimas 24 horas");

        List<EmailAudit> recentEmails = emailService.getRecentEmails();

        return ResponseEntity.ok(recentEmails);
    }

    /**
     * Endpoint de health check
     */
    @GetMapping("/health")
    @Operation(summary = "Verificar status do serviço de email", description = "Endpoint de health check para verificar se o serviço está funcionando")
    @ApiResponse(responseCode = "200", description = "Serviço funcionando normalmente")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Serviço de email funcionando normalmente");
    }

    /**
     * Endpoint raiz - redireciona para documentação Swagger
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    @Operation(summary = "Página inicial", description = "Redireciona para a documentação da API")
    @ApiResponse(responseCode = "302", description = "Redirecionamento para Swagger UI")
    public ResponseEntity<Void> home() {
        return ResponseEntity.status(302)
                .header("Location", "/swagger-ui/index.html")
                .build();
    }
}