package com.join.Mcqueen.repositories;

import com.join.Mcqueen.domain.models.EmailAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositório para auditoria de emails
 */
@Repository
public interface EmailAuditRepository extends JpaRepository<EmailAudit, Long> {

    /**
     * Busca auditorias por período
     */
    List<EmailAudit> findBySendDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Busca auditorias por destinatário
     */
    List<EmailAudit> findByRecipientContainingIgnoreCase(String recipient);

    /**
     * Busca auditorias por órgão
     */
    List<EmailAudit> findByOrganizationNameContainingIgnoreCase(String organizationName);

    /**
     * Conta emails enviados por status
     */
    @Query("SELECT e.sendStatus, COUNT(e) FROM EmailAudit e GROUP BY e.sendStatus")
    List<Object[]> countByStatus();

    /**
     * Busca emails enviados nas últimas horas
     */
    @Query("SELECT e FROM EmailAudit e WHERE e.sendDate >= :limitDate ORDER BY e.sendDate DESC")
    List<EmailAudit> findRecentEmails(@Param("limitDate") LocalDateTime limitDate);
}