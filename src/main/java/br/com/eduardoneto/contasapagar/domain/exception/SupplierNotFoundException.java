package br.com.eduardoneto.contasapagar.domain.exception;

public class SupplierNotFoundException extends RuntimeException {
    public SupplierNotFoundException(Long id) {
        super("Fornecedor não encontrado com id: " + id);
    }
}
