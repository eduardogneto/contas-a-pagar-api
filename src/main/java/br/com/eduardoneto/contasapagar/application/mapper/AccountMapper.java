package br.com.eduardoneto.contasapagar.application.mapper;

import br.com.eduardoneto.contasapagar.application.dto.AccountResponseDTO;
import br.com.eduardoneto.contasapagar.application.dto.AccountSummaryDTO;
import br.com.eduardoneto.contasapagar.application.dto.SupplierResponseDTO;
import br.com.eduardoneto.contasapagar.domain.model.Account;
import br.com.eduardoneto.contasapagar.domain.model.Supplier;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(target = "supplier", source = "supplier")
    AccountResponseDTO toResponseDTO(Account account);

    @Mapping(target = "supplierName", source = "supplier.name")
    AccountSummaryDTO toSummaryDTO(Account account);

    SupplierResponseDTO toSupplierResponseDTO(Supplier supplier);
}
