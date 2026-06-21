package br.com.eduardoneto.contasapagar.application.usecase;

import br.com.eduardoneto.contasapagar.application.dto.AccountRequestDTO;
import br.com.eduardoneto.contasapagar.application.dto.AccountResponseDTO;
import br.com.eduardoneto.contasapagar.application.mapper.AccountMapper;
import br.com.eduardoneto.contasapagar.domain.exception.SupplierNotFoundException;
import br.com.eduardoneto.contasapagar.domain.model.Account;
import br.com.eduardoneto.contasapagar.domain.model.Supplier;
import br.com.eduardoneto.contasapagar.domain.repository.AccountRepository;
import br.com.eduardoneto.contasapagar.domain.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateAccountUseCase {

    private final AccountRepository accountRepository;
    private final SupplierRepository supplierRepository;
    private final AccountMapper accountMapper;

    @Transactional
    public AccountResponseDTO execute(AccountRequestDTO dto) {
        Supplier supplier = supplierRepository.findById(dto.supplierId())
                .orElseThrow(() -> new SupplierNotFoundException(dto.supplierId()));

        Account account = new Account(dto.dueDate(), dto.amount(), dto.description(), supplier);
        Account saved = accountRepository.save(account);

        return accountMapper.toResponseDTO(saved);
    }
}
