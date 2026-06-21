package br.com.eduardoneto.contasapagar.application.dto;

import java.util.UUID;

public record CsvImportMessageDTO(
        UUID protocol,
        String csvContent
) {}
