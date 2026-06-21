package br.com.eduardoneto.contasapagar.application.usecase;

import br.com.eduardoneto.contasapagar.application.dto.AccountResponseDTO;
import br.com.eduardoneto.contasapagar.application.dto.ChangeStatusDTO;
import br.com.eduardoneto.contasapagar.application.mapper.AccountMapper;
import br.com.eduardoneto.contasapagar.domain.exception.AccountNotFoundException;
import br.com.eduardoneto.contasapagar.domain.model.Account;
import br.com.eduardoneto.contasapagar.domain.model.Status;
import br.com.eduardoneto.contasapagar.domain.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChangeAccountStatusUseCase {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @Transactional
    public AccountResponseDTO execute(UUID id, ChangeStatusDTO dto) {
        Account account = accountRepository.findWithSupplierById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));

        if (dto.status() == Status.PAID) {
            LocalDate paymentDate = dto.paymentDate() != null ? dto.paymentDate() : LocalDate.now();
            account.pay(paymentDate);
        } else if (dto.status() == Status.CANCELLED) {
            account.cancel();
        }

        return accountMapper.toResponseDTO(account);
    }
}
