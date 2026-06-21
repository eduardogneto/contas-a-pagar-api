package br.com.eduardoneto.contasapagar.domain.repository;

import br.com.eduardoneto.contasapagar.domain.model.Account;
import br.com.eduardoneto.contasapagar.domain.model.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepository {
    Account save(Account account);
    Optional<Account> findById(UUID id);
    Optional<Account> findWithSupplierById(UUID id);
    boolean existsById(UUID id);
    void deleteById(UUID id);
    Page<Account> findAllWithFilters(LocalDate dueDate, String description, Pageable pageable);
    BigDecimal sumPaidAmountByPeriod(Status status, LocalDate start, LocalDate end);
}
