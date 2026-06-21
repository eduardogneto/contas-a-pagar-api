package br.com.eduardoneto.contasapagar.application.usecase;

import br.com.eduardoneto.contasapagar.application.dto.AccountSummaryDTO;
import br.com.eduardoneto.contasapagar.application.mapper.AccountMapper;
import br.com.eduardoneto.contasapagar.domain.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ListAccountsUseCase {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @Transactional(readOnly = true)
    public Page<AccountSummaryDTO> execute(LocalDate dueDate, String description, Pageable pageable) {
        return accountRepository.findAllWithFilters(dueDate, description, pageable)
                .map(accountMapper::toSummaryDTO);
    }
}
