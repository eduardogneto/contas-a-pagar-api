package br.com.eduardoneto.contasapagar.application.usecase;

import br.com.eduardoneto.contasapagar.domain.exception.AccountNotFoundException;
import br.com.eduardoneto.contasapagar.domain.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteAccountUseCase {

    private final AccountRepository accountRepository;

    @Transactional
    public void execute(UUID id) {
        if (!accountRepository.existsById(id)) {
            throw new AccountNotFoundException(id);
        }
        accountRepository.deleteById(id);
    }
}
