package br.com.eduardoneto.contasapagar.domain.model;

import br.com.eduardoneto.contasapagar.domain.exception.AccountAlreadyPaidException;
import br.com.eduardoneto.contasapagar.domain.exception.AccountCancelledException;
import br.com.eduardoneto.contasapagar.domain.exception.InvalidAmountException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

class AccountTest {

    private Supplier supplier;

    @BeforeEach
    void setUp() {
        supplier = new Supplier("Test Supplier");
    }

    @Nested
    @DisplayName("Constructor / factory")
    class Constructor {

        @Test
        @DisplayName("deve criar conta com situacao PENDING")
        void shouldCreateAccountWithPendingStatus() {
            Account account = new Account(LocalDate.now().plusDays(10), BigDecimal.TEN, "Rent", supplier);
            assertThat(account.getStatus()).isEqualTo(Status.PENDING);
        }

        @Test
        @DisplayName("deve lançar InvalidAmountException para valor zero")
        void shouldThrowForZeroAmount() {
            assertThatThrownBy(() -> new Account(LocalDate.now(), BigDecimal.ZERO, "desc", supplier))
                    .isInstanceOf(InvalidAmountException.class);
        }

        @Test
        @DisplayName("deve lançar InvalidAmountException para valor negativo")
        void shouldThrowForNegativeAmount() {
            assertThatThrownBy(() -> new Account(LocalDate.now(), BigDecimal.valueOf(-1), "desc", supplier))
                    .isInstanceOf(InvalidAmountException.class);
        }

        @Test
        @DisplayName("deve lançar InvalidAmountException para valor nulo")
        void shouldThrowForNullAmount() {
            assertThatThrownBy(() -> new Account(LocalDate.now(), null, "desc", supplier))
                    .isInstanceOf(InvalidAmountException.class);
        }
    }

    @Nested
    @DisplayName("pay()")
    class Pay {

        @Test
        @DisplayName("deve pagar conta pendente")
        void shouldPayPendingAccount() {
            Account account = new Account(LocalDate.now().plusDays(5), BigDecimal.TEN, "Electricity", supplier);
            LocalDate paymentDate = LocalDate.now();
            account.pay(paymentDate);

            assertThat(account.getStatus()).isEqualTo(Status.PAID);
            assertThat(account.getPaymentDate()).isEqualTo(paymentDate);
        }

        @Test
        @DisplayName("deve lançar AccountAlreadyPaidException ao pagar conta já paga")
        void shouldThrowWhenPayingAlreadyPaidAccount() {
            Account account = new Account(LocalDate.now().plusDays(5), BigDecimal.TEN, "Electricity", supplier);
            account.pay(LocalDate.now());

            assertThatThrownBy(() -> account.pay(LocalDate.now()))
                    .isInstanceOf(AccountAlreadyPaidException.class);
        }

        @Test
        @DisplayName("deve lançar AccountCancelledException ao pagar conta cancelada")
        void shouldThrowWhenPayingCancelledAccount() {
            Account account = new Account(LocalDate.now().plusDays(5), BigDecimal.TEN, "Electricity", supplier);
            account.cancel();

            assertThatThrownBy(() -> account.pay(LocalDate.now()))
                    .isInstanceOf(AccountCancelledException.class);
        }
    }

    @Nested
    @DisplayName("cancel()")
    class Cancel {

        @Test
        @DisplayName("deve cancelar conta pendente")
        void shouldCancelPendingAccount() {
            Account account = new Account(LocalDate.now().plusDays(5), BigDecimal.TEN, "Internet", supplier);
            account.cancel();

            assertThat(account.getStatus()).isEqualTo(Status.CANCELLED);
        }

        @Test
        @DisplayName("deve lançar AccountAlreadyPaidException ao cancelar conta paga")
        void shouldThrowWhenCancellingPaidAccount() {
            Account account = new Account(LocalDate.now().plusDays(5), BigDecimal.TEN, "Internet", supplier);
            account.pay(LocalDate.now());

            assertThatThrownBy(() -> account.cancel())
                    .isInstanceOf(AccountAlreadyPaidException.class);
        }
    }
}
