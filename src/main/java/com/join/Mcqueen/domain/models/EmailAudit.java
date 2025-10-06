package com.join.Mcqueen.domain.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidade para auditoria de emails enviados
 */
@Entity
@Table(name = "email_audit")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "recipient", nullable = false)
    private String recipient;

    @Column(name = "email_subject", nullable = false)
    private String emailSubject;

    @Column(name = "organization_name", nullable = false)
    private String organizationName;

    @Column(name = "responsible_contact", nullable = false)
    private String responsibleContact;

    @Column(name = "subject", nullable = false)
    private String subject;

    @Column(name = "reference_period", nullable = false)
    private String referencePeriod;

    @Column(name = "data_description", columnDefinition = "TEXT")
    private String dataDescription;

    @Column(name = "file_count")
    private Integer fileCount;

    @Column(name = "file_names", columnDefinition = "TEXT")
    private String fileNames;

    @Column(name = "send_status", nullable = false)
    private String sendStatus;

    @Column(name = "send_date", nullable = false)
    private LocalDateTime sendDate;

    @Column(name = "lgpd_compliance")
    private Boolean lgpdCompliance;

    @PrePersist
    protected void onCreate() {
        sendDate = LocalDateTime.now();
    }
}