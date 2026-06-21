package br.com.eduardoneto.contasapagar.application.dto;

import br.com.eduardoneto.contasapagar.domain.model.Status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record AccountSummaryDTO(
        UUID id,
        LocalDate dueDate,
        BigDecimal amount,
        String description,
        Status status,
        String supplierName
) {}
