package br.com.eduardoneto.contasapagar.infrastructure.persistence;

import br.com.eduardoneto.contasapagar.domain.model.Supplier;
import br.com.eduardoneto.contasapagar.domain.repository.SupplierRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierJpaRepository extends JpaRepository<Supplier, Long>, SupplierRepository {
}
