package br.com.eduardoneto.contasapagar.domain.repository;

import br.com.eduardoneto.contasapagar.domain.model.Supplier;

import java.util.List;
import java.util.Optional;

public interface SupplierRepository {
    Optional<Supplier> findById(Long id);
    Supplier save(Supplier supplier);
    List<Supplier> findAll();
}
