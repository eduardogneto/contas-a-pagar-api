package br.com.eduardoneto.contasapagar.application.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PaidAmountReportDTO(
        LocalDate start,
        LocalDate end,
        BigDecimal totalPaid
) {}
