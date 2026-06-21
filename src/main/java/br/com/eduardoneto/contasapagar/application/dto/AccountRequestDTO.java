package br.com.eduardoneto.contasapagar.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AccountRequestDTO(

        @NotNull(message = "Data de vencimento é obrigatória")
        LocalDate dueDate,

        @NotNull(message = "Valor é obrigatório")
        @Positive(message = "Valor deve ser positivo")
        BigDecimal amount,

        @NotBlank(message = "Descrição é obrigatória")
        String description,

        @NotNull(message = "Fornecedor é obrigatório")
        Long supplierId
) {}
