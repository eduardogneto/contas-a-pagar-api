package br.com.eduardoneto.contasapagar.application.usecase;

import br.com.eduardoneto.contasapagar.application.dto.PaidAmountReportDTO;
import br.com.eduardoneto.contasapagar.domain.model.Status;
import br.com.eduardoneto.contasapagar.domain.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class GetPaidAmountReportUseCase {

    private final AccountRepository accountRepository;

    @Transactional(readOnly = true)
    public PaidAmountReportDTO execute(LocalDate start, LocalDate end) {
        BigDecimal total = accountRepository.sumPaidAmountByPeriod(Status.PAID, start, end);
        return new PaidAmountReportDTO(start, end, total);
    }
}
