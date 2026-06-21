package br.com.eduardoneto.contasapagar.infrastructure.messaging;

import br.com.eduardoneto.contasapagar.application.dto.CsvImportMessageDTO;
import br.com.eduardoneto.contasapagar.domain.model.Account;
import br.com.eduardoneto.contasapagar.domain.model.Supplier;
import br.com.eduardoneto.contasapagar.infrastructure.config.KafkaConfig;
import br.com.eduardoneto.contasapagar.domain.repository.AccountRepository;
import br.com.eduardoneto.contasapagar.domain.repository.SupplierRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class CsvImportConsumer {

    private final AccountRepository accountRepository;
    private final SupplierRepository supplierRepository;

    @KafkaListener(topics = KafkaConfig.CSV_IMPORT_TOPIC, groupId = "csv-import-group")
    @Transactional
    public void consume(CsvImportMessageDTO message) {
        log.info("Processing CSV import — protocol={}", message.protocol());

        try (CSVReader reader = new CSVReader(new StringReader(message.csvContent()))) {
            String[] header = reader.readNext();
            if (header == null) {
                log.warn("Empty CSV file — protocol={}", message.protocol());
                return;
            }

            String[] fields;
            int lineNumber = 1;

            while ((fields = reader.readNext()) != null) {
                lineNumber++;
                try {
                    processLine(fields, lineNumber, message.protocol().toString());
                } catch (Exception ex) {
                    log.error("Protocol={} — line {} failed: {}", message.protocol(), lineNumber, ex.getMessage());
                }
            }

        } catch (IOException | CsvValidationException ex) {
            log.error("Error reading CSV — protocol={}: {}", message.protocol(), ex.getMessage());
        }

        log.info("CSV import completed — protocol={}", message.protocol());
    }

    private void processLine(String[] fields, int lineNumber, String protocol) {
        if (fields.length < 4) {
            throw new IllegalArgumentException(
                    "Número insuficiente de colunas: esperado 4, encontrado " + fields.length);
        }

        LocalDate dueDate = parseDate(fields[0].trim(), "dueDate", lineNumber);
        BigDecimal amount = parseAmount(fields[1].trim(), lineNumber);
        String description = fields[2].trim();
        Long supplierId = parseLong(fields[3].trim(), "supplierId", lineNumber);

        if (description.isBlank()) {
            throw new IllegalArgumentException("Descrição não pode ser vazia na linha " + lineNumber);
        }

        Optional<Supplier> supplierOpt = supplierRepository.findById(supplierId);
        if (supplierOpt.isEmpty()) {
            throw new IllegalArgumentException(
                    "Fornecedor com id=" + supplierId + " não encontrado na linha " + lineNumber);
        }

        Account account = new Account(dueDate, amount, description, supplierOpt.get());
        accountRepository.save(account);

        log.debug("Protocol={} — line {} imported successfully", protocol, lineNumber);
    }

    private LocalDate parseDate(String value, String field, int line) {
        try {
            return LocalDate.parse(value);
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException(
                    "Campo '" + field + "' inválido na linha " + line + ": '" + value + "'. Use o formato yyyy-MM-dd");
        }
    }

    private BigDecimal parseAmount(String value, int line) {
        try {
            BigDecimal bd = new BigDecimal(value);
            if (bd.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Valor deve ser positivo na linha " + line + ": " + value);
            }
            return bd;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Valor inválido na linha " + line + ": '" + value + "'");
        }
    }

    private Long parseLong(String value, String field, int line) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(
                    "Campo '" + field + "' inválido na linha " + line + ": '" + value + "'");
        }
    }
}
