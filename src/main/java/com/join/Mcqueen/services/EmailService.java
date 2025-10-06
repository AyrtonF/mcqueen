package com.join.Mcqueen.services;

import com.join.Mcqueen.domain.dtos.EmailFormDTO;
import com.join.Mcqueen.domain.dtos.EmailResponseDTO;
import com.join.Mcqueen.domain.models.EmailAudit;
import com.join.Mcqueen.domain.models.EmailData;
import com.join.Mcqueen.exceptions.EmailSendingException;
import com.join.Mcqueen.exceptions.FileProcessingException;
import com.join.Mcqueen.mappers.EmailMapper;
import com.join.Mcqueen.repositories.EmailAuditRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Serviço responsável pelo envio de emails com anexos
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final EmailAuditRepository emailAuditRepository;
    private final EmailMapper emailMapper;

    @Value("${app.email.destinatario.padrao:gabriel.almeida1@sad.pe.gov.br}")
    private String defaultRecipient;

    /**
     * Envia email com dados do formulário e anexos CSV
     */
    public EmailResponseDTO sendFormEmail(EmailFormDTO emailFormDTO, List<MultipartFile> files,
            String recipient) {
        log.info("Iniciando envio de email para: {}", recipient != null ? recipient : defaultRecipient);

        try {
            // Validar arquivos CSV
            validateCsvFiles(files);

            // Criar dados do email
            EmailData emailData = EmailData.builder()
                    .recipient(recipient != null ? recipient : defaultRecipient)
                    .emailSubject(createEmailSubject(emailFormDTO))
                    .message(createEmailBody(emailFormDTO))
                    .files(files)
                    .organizationName(emailFormDTO.getOrganizationName())
                    .responsibleContact(emailFormDTO.getResponsibleContact())
                    .subject(emailFormDTO.getSubject())
                    .referencePeriod(emailFormDTO.getReferencePeriod())
                    .dataDescription(emailFormDTO.getDataDescription())
                    .lgpdCompliance(emailFormDTO.getLgpdCompliance())
                    .build();

            // Enviar email
            sendEmail(emailData);

            // Salvar auditoria
            saveAudit(emailData, "SUCCESS");

            // Criar resposta
            return EmailResponseDTO.builder()
                    .status("SUCCESS")
                    .message("Email enviado com sucesso")
                    .sendDate(LocalDateTime.now())
                    .recipient(emailData.getRecipient())
                    .fileCount(files != null ? files.size() : 0)
                    .fileNames(files != null ? files.stream()
                            .map(MultipartFile::getOriginalFilename)
                            .collect(Collectors.toList()) : List.of())
                    .build();

        } catch (Exception e) {
            log.error("Erro ao enviar email: {}", e.getMessage(), e);

            // Salvar auditoria de erro
            EmailData emailDataError = EmailData.builder()
                    .recipient(recipient != null ? recipient : defaultRecipient)
                    .organizationName(emailFormDTO.getOrganizationName())
                    .responsibleContact(emailFormDTO.getResponsibleContact())
                    .subject(emailFormDTO.getSubject())
                    .referencePeriod(emailFormDTO.getReferencePeriod())
                    .dataDescription(emailFormDTO.getDataDescription())
                    .lgpdCompliance(emailFormDTO.getLgpdCompliance())
                    .files(files)
                    .build();

            saveAudit(emailDataError, "ERROR: " + e.getMessage());

            throw new EmailSendingException("Falha ao enviar email: " + e.getMessage(), e);
        }
    }

    /**
     * Busca histórico de emails por período
     */
    public List<EmailAudit> getEmailHistory(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate != null && endDate != null) {
            return emailAuditRepository.findBySendDateBetween(startDate, endDate);
        }
        return emailAuditRepository.findAll();
    }

    /**
     * Busca emails recentes (últimas 24h)
     */
    public List<EmailAudit> getRecentEmails() {
        return emailAuditRepository.findRecentEmails(LocalDateTime.now().minusDays(1));
    }

    /**
     * Valida se os arquivos são CSVs
     */
    private void validateCsvFiles(List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            throw new FileProcessingException("Pelo menos um arquivo CSV deve ser enviado");
        }

        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                throw new FileProcessingException("Arquivo vazio detectado");
            }

            String fileName = file.getOriginalFilename();
            if (fileName == null || !fileName.toLowerCase().endsWith(".csv")) {
                throw new FileProcessingException(
                        "Arquivo " + fileName + " não é um CSV válido. Apenas arquivos .csv são aceitos.");
            }

            // Validar tamanho do arquivo (10MB máximo por arquivo)
            if (file.getSize() > 10 * 1024 * 1024) {
                throw new FileProcessingException(
                        "Arquivo " + fileName + " excede o tamanho máximo de 10MB.");
            }
        }
    }

    /**
     * Cria assunto do email baseado no formulário
     */
    private String createEmailSubject(EmailFormDTO emailFormDTO) {
        return String.format("Formulário: %s - Órgão: %s",
                emailFormDTO.getSubject(),
                emailFormDTO.getOrganizationName());
    }

    /**
     * Cria corpo do email em HTML
     */
    private String createEmailBody(EmailFormDTO emailFormDTO) {
        StringBuilder corpo = new StringBuilder();
        corpo.append("<!DOCTYPE html>");
        corpo.append("<html><head><meta charset='UTF-8'><title>Dados do Formulário</title></head><body>");
        corpo.append("<h2>Dados do Formulário Enviado</h2>");

        corpo.append("<p><strong>Nome do Órgão:</strong> ").append(emailFormDTO.getOrganizationName()).append("</p>");
        corpo.append("<p><strong>Contato do Responsável:</strong> ").append(emailFormDTO.getResponsibleContact())
                .append("</p>");

        corpo.append("<p><strong>Tema:</strong> ").append(emailFormDTO.getSubject()).append("</p>");
        corpo.append("<p><strong>Período de Referência:</strong> ").append(emailFormDTO.getReferencePeriod())
                .append("</p>");

        corpo.append("<h3>Descrição dos Dados:</h3>");
        corpo.append("<p>").append(emailFormDTO.getDataDescription().replace("\n", "<br>")).append("</p>");

        if (Boolean.TRUE.equals(emailFormDTO.getLgpdCompliance())) {
            corpo.append("<p><strong>LGPD:</strong> Dados em conformidade com a LGPD ✓</p>");
        }

        corpo.append("<p><em>Os arquivos CSV estão anexados a este email.</em></p>");
        corpo.append("<hr>");
        corpo.append("<p><small>Email enviado automaticamente pelo Sistema Mcqueen</small></p>");
        corpo.append("</body></html>");

        return corpo.toString();
    }

    /**
     * Envia o email propriamente dito
     */
    private void sendEmail(EmailData emailData) throws MessagingException, IOException {
        log.info("Preparando envio de email para: {}", emailData.getRecipient());

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setFrom("gabriel.almeida1@sad.pe.gov.br"); // Usar mesmo email da autenticação
        helper.setTo(emailData.getRecipient());
        helper.setSubject(emailData.getEmailSubject());
        helper.setText(emailData.getMessage(), true);

        // Anexar arquivos CSV
        if (emailData.getFiles() != null && !emailData.getFiles().isEmpty()) {
            for (MultipartFile file : emailData.getFiles()) {
                ByteArrayResource resource = new ByteArrayResource(file.getBytes());
                helper.addAttachment(file.getOriginalFilename(), resource);
            }
        }

        mailSender.send(mimeMessage);
        log.info("Email enviado com sucesso para: {}", emailData.getRecipient());
    }

    /**
     * Salva registro de auditoria do envio
     */
    private void saveAudit(EmailData emailData, String status) {
        try {
            EmailAudit audit = emailMapper.toEmailAudit(emailData, status);
            emailAuditRepository.save(audit);
        } catch (Exception e) {
            log.error("Erro ao salvar auditoria: {}", e.getMessage(), e);
        }
    }
}