package br.com.eduardoneto.contasapagar.application.usecase;

import br.com.eduardoneto.contasapagar.application.dto.AccountRequestDTO;
import br.com.eduardoneto.contasapagar.application.dto.AccountResponseDTO;
import br.com.eduardoneto.contasapagar.application.mapper.AccountMapper;
import br.com.eduardoneto.contasapagar.domain.exception.SupplierNotFoundException;
import br.com.eduardoneto.contasapagar.domain.model.Account;
import br.com.eduardoneto.contasapagar.domain.model.Supplier;
import br.com.eduardoneto.contasapagar.domain.repository.AccountRepository;
import br.com.eduardoneto.contasapagar.domain.repository.SupplierRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateAccountUseCaseTest {

    @Mock AccountRepository accountRepository;
    @Mock SupplierRepository supplierRepository;
    @Mock AccountMapper accountMapper;

    @InjectMocks CreateAccountUseCase useCase;

    @Test
    void shouldCreateAccountSuccessfully() {
        AccountRequestDTO dto = new AccountRequestDTO(
                LocalDate.now().plusDays(10), BigDecimal.TEN, "Rent", 1L);
        Supplier supplier = new Supplier("Supplier A");

        when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier));
        when(accountRepository.save(any(Account.class))).thenAnswer(inv -> inv.getArgument(0));
        when(accountMapper.toResponseDTO(any(Account.class))).thenReturn(mock(AccountResponseDTO.class));

        AccountResponseDTO result = useCase.execute(dto);

        assertThat(result).isNotNull();
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void shouldThrowWhenSupplierNotFound() {
        AccountRequestDTO dto = new AccountRequestDTO(
                LocalDate.now().plusDays(10), BigDecimal.TEN, "Rent", 99L);

        when(supplierRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(dto))
                .isInstanceOf(SupplierNotFoundException.class);

        verify(accountRepository, never()).save(any());
    }
}
