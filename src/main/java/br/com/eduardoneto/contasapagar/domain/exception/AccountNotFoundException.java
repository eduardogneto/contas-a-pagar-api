package br.com.eduardoneto.contasapagar.domain.exception;

import java.util.UUID;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(UUID id) {
        super("Conta não encontrada com id: " + id);
    }
}
