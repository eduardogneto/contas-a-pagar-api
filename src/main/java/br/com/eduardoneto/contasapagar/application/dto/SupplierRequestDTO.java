package br.com.eduardoneto.contasapagar.application.dto;

import jakarta.validation.constraints.NotBlank;

public record SupplierRequestDTO(@NotBlank String name) {}
