package br.com.eduardoneto.contasapagar.infrastructure.persistence;

import br.com.eduardoneto.contasapagar.domain.model.Account;
import br.com.eduardoneto.contasapagar.domain.model.Status;
import br.com.eduardoneto.contasapagar.domain.repository.AccountRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountJpaRepository extends JpaRepository<Account, UUID>, AccountRepository {

    @EntityGraph(attributePaths = "supplier")
    Optional<Account> findWithSupplierById(UUID id);

    @EntityGraph(attributePaths = "supplier")
    @Query("""
            SELECT a FROM Account a
            WHERE (:dueDate IS NULL OR a.dueDate = :dueDate)
              AND (:description IS NULL OR LOWER(a.description) LIKE LOWER(CONCAT('%', :description, '%')))
            """)
    Page<Account> findAllWithFilters(
            @Param("dueDate") LocalDate dueDate,
            @Param("description") String description,
            Pageable pageable
    );

    @Query("""
            SELECT COALESCE(SUM(a.amount), 0)
            FROM Account a
            WHERE a.status = :status
              AND a.paymentDate >= :start
              AND a.paymentDate <= :end
            """)
    BigDecimal sumPaidAmountByPeriod(
            @Param("status") Status status,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end
    );
}
