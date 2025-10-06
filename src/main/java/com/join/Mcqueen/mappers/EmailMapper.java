package com.join.Mcqueen.mappers;

import com.join.Mcqueen.domain.dtos.EmailFormDTO;
import com.join.Mcqueen.domain.models.EmailAudit;
import com.join.Mcqueen.domain.models.EmailData;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para conversões entre DTOs e entidades de email
 */
@Component
public class EmailMapper {

    /**
     * Converte EmailFormDTO para EmailData
     */
    public EmailData toEmailData(EmailFormDTO dto, List<MultipartFile> files, String recipient) {
        if (dto == null) {
            return null;
        }

        return EmailData.builder()
                .organizationName(dto.getOrganizationName())
                .responsibleContact(dto.getResponsibleContact())
                .subject(dto.getSubject())
                .referencePeriod(dto.getReferencePeriod())
                .dataDescription(dto.getDataDescription())
                .lgpdCompliance(dto.getLgpdCompliance())
                .files(files)
                .recipient(recipient)
                .emailSubject(createSubject(dto))
                .message(createMessage(dto))
                .build();
    }

    /**
     * Converte EmailData para EmailAudit
     */
    public EmailAudit toEmailAudit(EmailData emailData, String status) {
        if (emailData == null) {
            return null;
        }

        return EmailAudit.builder()
                .recipient(emailData.getRecipient())
                .emailSubject(
                        emailData.getEmailSubject() != null ? emailData.getEmailSubject() : createSubject(emailData))
                .organizationName(emailData.getOrganizationName())
                .responsibleContact(emailData.getResponsibleContact())
                .subject(emailData.getSubject())
                .referencePeriod(emailData.getReferencePeriod())
                .dataDescription(emailData.getDataDescription())
                .lgpdCompliance(emailData.getLgpdCompliance())
                .fileCount(emailData.getFiles() != null ? emailData.getFiles().size() : 0)
                .fileNames(extractFileNames(emailData.getFiles()))
                .sendStatus(status)
                .sendDate(LocalDateTime.now())
                .build();
    }

    /**
     * Converte EmailFormDTO para EmailAudit (para casos de erro)
     */
    public EmailAudit toEmailAuditFromDTO(EmailFormDTO dto, List<MultipartFile> files, String recipient,
            String status) {
        if (dto == null) {
            return null;
        }

        return EmailAudit.builder()
                .recipient(recipient)
                .emailSubject(createSubject(dto))
                .organizationName(dto.getOrganizationName())
                .responsibleContact(dto.getResponsibleContact())
                .subject(dto.getSubject())
                .referencePeriod(dto.getReferencePeriod())
                .dataDescription(dto.getDataDescription())
                .lgpdCompliance(dto.getLgpdCompliance())
                .fileCount(files != null ? files.size() : 0)
                .fileNames(extractFileNames(files))
                .sendStatus(status)
                .sendDate(LocalDateTime.now())
                .build();
    }

    /**
     * Cria assunto baseado nos dados do formulário
     */
    private String createSubject(EmailFormDTO dto) {
        if (dto == null || dto.getSubject() == null || dto.getOrganizationName() == null) {
            return "Envio de Dados - Sistema Mcqueen";
        }
        return String.format("Envio de Dados - %s - %s", dto.getSubject(), dto.getOrganizationName());
    }

    /**
     * Cria assunto baseado nos dados do EmailData
     */
    private String createSubject(EmailData emailData) {
        if (emailData == null || emailData.getSubject() == null || emailData.getOrganizationName() == null) {
            return "Envio de Dados - Sistema Mcqueen";
        }
        return String.format("Envio de Dados - %s - %s", emailData.getSubject(), emailData.getOrganizationName());
    }

    /**
     * Cria mensagem baseada nos dados do formulário
     */
    private String createMessage(EmailFormDTO dto) {
        if (dto == null) {
            return "";
        }

        StringBuilder message = new StringBuilder();
        message.append("Dados do Formulário:\n\n");
        message.append("Órgão: ").append(dto.getOrganizationName()).append("\n");
        message.append("Responsável: ").append(dto.getResponsibleContact()).append("\n");
        message.append("Tema: ").append(dto.getSubject()).append("\n");
        message.append("Período: ").append(dto.getReferencePeriod()).append("\n");
        message.append("Descrição: ").append(dto.getDataDescription()).append("\n");

        if (Boolean.TRUE.equals(dto.getLgpdCompliance())) {
            message.append("LGPD: Dados em conformidade confirmados\n");
        }

        return message.toString();
    }

    /**
     * Extrai nomes dos arquivos como string separada por vírgulas
     */
    private String extractFileNames(List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            return "";
        }

        return files.stream()
                .map(MultipartFile::getOriginalFilename)
                .filter(fileName -> fileName != null && !fileName.trim().isEmpty())
                .collect(Collectors.joining(", "));
    }
}