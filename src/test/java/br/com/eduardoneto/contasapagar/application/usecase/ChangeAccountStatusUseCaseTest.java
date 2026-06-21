package br.com.eduardoneto.contasapagar.application.usecase;

import br.com.eduardoneto.contasapagar.application.dto.AccountResponseDTO;
import br.com.eduardoneto.contasapagar.application.dto.ChangeStatusDTO;
import br.com.eduardoneto.contasapagar.application.mapper.AccountMapper;
import br.com.eduardoneto.contasapagar.domain.exception.AccountNotFoundException;
import br.com.eduardoneto.contasapagar.domain.model.Account;
import br.com.eduardoneto.contasapagar.domain.model.Status;
import br.com.eduardoneto.contasapagar.domain.model.Supplier;
import br.com.eduardoneto.contasapagar.domain.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChangeAccountStatusUseCaseTest {

    @Mock AccountRepository accountRepository;
    @Mock AccountMapper accountMapper;

    @InjectMocks ChangeAccountStatusUseCase useCase;

    private Account pendingAccount() {
        return new Account(LocalDate.now().plusDays(5), BigDecimal.TEN, "Internet", new Supplier("Supplier"));
    }

    @Test
    void shouldPayPendingAccount() {
        UUID id = UUID.randomUUID();
        Account account = pendingAccount();
        ChangeStatusDTO dto = new ChangeStatusDTO(Status.PAID, LocalDate.now());

        when(accountRepository.findWithSupplierById(id)).thenReturn(Optional.of(account));
        when(accountMapper.toResponseDTO(any())).thenReturn(mock(AccountResponseDTO.class));

        useCase.execute(id, dto);

        assertThat(account.getStatus()).isEqualTo(Status.PAID);
    }

    @Test
    void shouldCancelPendingAccount() {
        UUID id = UUID.randomUUID();
        Account account = pendingAccount();
        ChangeStatusDTO dto = new ChangeStatusDTO(Status.CANCELLED, null);

        when(accountRepository.findWithSupplierById(id)).thenReturn(Optional.of(account));
        when(accountMapper.toResponseDTO(any())).thenReturn(mock(AccountResponseDTO.class));

        useCase.execute(id, dto);

        assertThat(account.getStatus()).isEqualTo(Status.CANCELLED);
    }

    @Test
    void shouldThrowWhenAccountNotFound() {
        UUID id = UUID.randomUUID();
        when(accountRepository.findWithSupplierById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(id, new ChangeStatusDTO(Status.PAID, null)))
                .isInstanceOf(AccountNotFoundException.class);
    }
}
