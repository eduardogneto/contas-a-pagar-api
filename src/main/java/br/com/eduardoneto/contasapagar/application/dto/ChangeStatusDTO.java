package br.com.eduardoneto.contasapagar.application.dto;

import br.com.eduardoneto.contasapagar.domain.model.Status;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ChangeStatusDTO(

        @NotNull(message = "Situação é obrigatória")
        Status status,

        LocalDate paymentDate
) {}
