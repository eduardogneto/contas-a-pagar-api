package br.com.eduardoneto.contasapagar.application.usecase;

import br.com.eduardoneto.contasapagar.application.dto.AccountResponseDTO;
import br.com.eduardoneto.contasapagar.application.mapper.AccountMapper;
import br.com.eduardoneto.contasapagar.domain.exception.AccountNotFoundException;
import br.com.eduardoneto.contasapagar.domain.model.Account;
import br.com.eduardoneto.contasapagar.domain.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FindAccountUseCase {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @Transactional(readOnly = true)
    public AccountResponseDTO execute(UUID id) {
        Account account = accountRepository.findWithSupplierById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));
        return accountMapper.toResponseDTO(account);
    }
}
