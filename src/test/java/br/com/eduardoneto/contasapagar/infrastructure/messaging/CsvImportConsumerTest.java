package br.com.eduardoneto.contasapagar.infrastructure.messaging;

import br.com.eduardoneto.contasapagar.application.dto.CsvImportMessageDTO;
import br.com.eduardoneto.contasapagar.domain.model.Account;
import br.com.eduardoneto.contasapagar.domain.model.Supplier;
import br.com.eduardoneto.contasapagar.domain.repository.AccountRepository;
import br.com.eduardoneto.contasapagar.domain.repository.SupplierRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CsvImportConsumerTest {

    @Mock AccountRepository accountRepository;
    @Mock SupplierRepository supplierRepository;

    @InjectMocks CsvImportConsumer consumer;

    private static final String HEADER = "dueDate,amount,description,supplierId\n";

    @Test
    void shouldPersistValidLinesAndIgnoreInvalid() {
        Supplier supplier = new Supplier("Valid Supplier");
        String csv = HEADER
                + "2025-01-10,100.00,Rent,1\n"
                + "invalid-date,50.00,Electricity,1\n"
                + "2025-02-10,200.00,Internet,1\n";

        when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier));
        when(accountRepository.save(any(Account.class))).thenAnswer(inv -> inv.getArgument(0));

        consumer.consume(new CsvImportMessageDTO(UUID.randomUUID(), csv));

        verify(accountRepository, times(2)).save(any(Account.class));
    }

    @Test
    void shouldIgnoreLineWithNonExistentSupplier() {
        String csv = HEADER
                + "2025-01-10,100.00,Rent,999\n";

        when(supplierRepository.findById(999L)).thenReturn(Optional.empty());

        consumer.consume(new CsvImportMessageDTO(UUID.randomUUID(), csv));

        verify(accountRepository, never()).save(any());
    }

    @Test
    void shouldIgnoreLineWithNegativeAmount() {
        String csv = HEADER
                + "2025-01-10,-50.00,Rent,1\n";

        consumer.consume(new CsvImportMessageDTO(UUID.randomUUID(), csv));

        verify(accountRepository, never()).save(any());
    }

    @Test
    void shouldProcessEmptyCsvWithoutError() {
        String csv = HEADER;

        assertThatCode(() -> consumer.consume(new CsvImportMessageDTO(UUID.randomUUID(), csv)))
                .doesNotThrowAnyException();
        verify(accountRepository, never()).save(any());
    }

    @Test
    void shouldPersistAccountWithCorrectValues() {
        Supplier supplier = new Supplier("Supplier");
        String csv = HEADER
                + "2025-03-15,350.75,School Fee,1\n";

        when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier));
        when(accountRepository.save(any(Account.class))).thenAnswer(inv -> inv.getArgument(0));

        consumer.consume(new CsvImportMessageDTO(UUID.randomUUID(), csv));

        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(captor.capture());

        Account saved = captor.getValue();
        assertThat(saved.getDescription()).isEqualTo("School Fee");
        assertThat(saved.getAmount()).isEqualByComparingTo("350.75");
    }
}
