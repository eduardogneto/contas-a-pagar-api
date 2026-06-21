package br.com.eduardoneto.contasapagar.domain.model;

import br.com.eduardoneto.contasapagar.domain.exception.AccountAlreadyPaidException;
import br.com.eduardoneto.contasapagar.domain.exception.AccountCancelledException;
import br.com.eduardoneto.contasapagar.domain.exception.InvalidAmountException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "account")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "payment_date")
    private LocalDate paymentDate;

    @Column(name = "amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(name = "description", nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    public Account(LocalDate dueDate, BigDecimal amount, String description, Supplier supplier) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Valor da conta deve ser maior que zero.");
        }
        this.dueDate = dueDate;
        this.amount = amount;
        this.description = description;
        this.supplier = supplier;
        this.status = Status.PENDING;
    }

    public void pay(LocalDate paymentDate) {
        if (this.status == Status.PAID) {
            throw new AccountAlreadyPaidException("Conta já está paga.");
        }
        if (this.status == Status.CANCELLED) {
            throw new AccountCancelledException("Conta cancelada não pode ser paga.");
        }
        this.status = Status.PAID;
        this.paymentDate = paymentDate;
    }

    public void cancel() {
        if (this.status == Status.PAID) {
            throw new AccountAlreadyPaidException("Conta paga não pode ser cancelada.");
        }
        this.status = Status.CANCELLED;
    }

    public void update(LocalDate dueDate, BigDecimal amount, String description, Supplier supplier) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Valor da conta deve ser maior que zero.");
        }
        this.dueDate = dueDate;
        this.amount = amount;
        this.description = description;
        this.supplier = supplier;
    }
}
