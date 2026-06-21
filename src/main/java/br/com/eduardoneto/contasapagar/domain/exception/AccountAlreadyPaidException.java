package br.com.eduardoneto.contasapagar.domain.exception;

public class AccountAlreadyPaidException extends RuntimeException {
    public AccountAlreadyPaidException(String message) {
        super(message);
    }
}
