package br.com.eduardoneto.contasapagar.infrastructure.web;

import br.com.eduardoneto.contasapagar.application.dto.CsvImportMessageDTO;
import br.com.eduardoneto.contasapagar.infrastructure.config.KafkaConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/accounts/import")
@RequiredArgsConstructor
@Tag(name = "Import", description = "Asynchronous CSV import of accounts")
public class ImportController {

    private final KafkaTemplate<String, CsvImportMessageDTO> kafkaTemplate;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Import accounts via CSV (asynchronous)")
    public ResponseEntity<Map<String, String>> importCsv(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "CSV file cannot be empty"));
        }

        String content = new String(file.getBytes(), StandardCharsets.UTF_8);
        UUID protocol = UUID.randomUUID();

        CsvImportMessageDTO message = new CsvImportMessageDTO(protocol, content);
        kafkaTemplate.send(KafkaConfig.CSV_IMPORT_TOPIC, protocol.toString(), message);

        log.info("CSV import queued — protocol={}", protocol);

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(Map.of("protocol", protocol.toString()));
    }
}
