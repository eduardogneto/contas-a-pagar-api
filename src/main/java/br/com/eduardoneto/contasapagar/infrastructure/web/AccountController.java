package br.com.eduardoneto.contasapagar.infrastructure.web;

import br.com.eduardoneto.contasapagar.application.dto.*;
import br.com.eduardoneto.contasapagar.application.usecase.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
@Tag(name = "Accounts", description = "Account management")
public class AccountController {

    private final CreateAccountUseCase createAccountUseCase;
    private final FindAccountUseCase findAccountUseCase;
    private final UpdateAccountUseCase updateAccountUseCase;
    private final DeleteAccountUseCase deleteAccountUseCase;
    private final ChangeAccountStatusUseCase changeAccountStatusUseCase;
    private final ListAccountsUseCase listAccountsUseCase;
    private final GetPaidAmountReportUseCase getPaidAmountReportUseCase;

    @PostMapping
    @Operation(summary = "Create account")
    public ResponseEntity<AccountResponseDTO> create(@Valid @RequestBody AccountRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(createAccountUseCase.execute(dto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find account by ID")
    public ResponseEntity<AccountResponseDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(findAccountUseCase.execute(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update account")
    public ResponseEntity<AccountResponseDTO> update(
            @PathVariable UUID id,
            @Valid @RequestBody AccountRequestDTO dto) {
        return ResponseEntity.ok(updateAccountUseCase.execute(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete account")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        deleteAccountUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Change account status")
    public ResponseEntity<AccountResponseDTO> changeStatus(
            @PathVariable UUID id,
            @Valid @RequestBody ChangeStatusDTO dto) {
        return ResponseEntity.ok(changeAccountStatusUseCase.execute(id, dto));
    }

    @GetMapping
    @Operation(summary = "List accounts (paginated, with filters)")
    public ResponseEntity<Page<AccountSummaryDTO>> list(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dueDate,
            @RequestParam(required = false) String description,
            @PageableDefault(size = 20, sort = "dueDate") Pageable pageable) {
        return ResponseEntity.ok(listAccountsUseCase.execute(dueDate, description, pageable));
    }

    @GetMapping("/reports/paid-amount")
    @Operation(summary = "Report: total paid amount by period")
    public ResponseEntity<PaidAmountReportDTO> paidAmountReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return ResponseEntity.ok(getPaidAmountReportUseCase.execute(start, end));
    }
}
