package br.com.eduardoneto.contasapagar.domain.exception;

public class AccountCancelledException extends RuntimeException {
    public AccountCancelledException(String message) {
        super(message);
    }
}
