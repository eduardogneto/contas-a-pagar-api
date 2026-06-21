package br.com.eduardoneto.contasapagar.infrastructure.web;

import br.com.eduardoneto.contasapagar.application.dto.SupplierRequestDTO;
import br.com.eduardoneto.contasapagar.application.dto.SupplierResponseDTO;
import br.com.eduardoneto.contasapagar.domain.exception.SupplierNotFoundException;
import br.com.eduardoneto.contasapagar.domain.model.Supplier;
import br.com.eduardoneto.contasapagar.domain.repository.SupplierRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/suppliers")
@RequiredArgsConstructor
@Tag(name = "Suppliers", description = "Supplier management")
public class SupplierController {

    private final SupplierRepository supplierRepository;

    @PostMapping
    @Operation(summary = "Create supplier")
    public ResponseEntity<SupplierResponseDTO> create(@Valid @RequestBody SupplierRequestDTO dto) {
        Supplier saved = supplierRepository.save(new Supplier(dto.name()));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new SupplierResponseDTO(saved.getId(), saved.getName()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find supplier by ID")
    public ResponseEntity<SupplierResponseDTO> findById(@PathVariable Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new SupplierNotFoundException(id));
        return ResponseEntity.ok(new SupplierResponseDTO(supplier.getId(), supplier.getName()));
    }

    @GetMapping
    @Operation(summary = "List all suppliers")
    public ResponseEntity<List<SupplierResponseDTO>> list() {
        List<SupplierResponseDTO> list = supplierRepository.findAll().stream()
                .map(s -> new SupplierResponseDTO(s.getId(), s.getName()))
                .toList();
        return ResponseEntity.ok(list);
    }
}
