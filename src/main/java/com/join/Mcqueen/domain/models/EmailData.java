package com.join.Mcqueen.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Modelo para dados completos do email a ser enviado
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailData {

    private String recipient;
    private String emailSubject;
    private String message;
    private List<MultipartFile> files;
    private String organizationName;
    private String responsibleContact;
    private String subject;
    private String referencePeriod;
    private String dataDescription;
    private Boolean lgpdCompliance;
}