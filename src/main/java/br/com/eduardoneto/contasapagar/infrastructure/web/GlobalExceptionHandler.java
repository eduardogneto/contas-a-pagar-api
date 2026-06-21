package br.com.eduardoneto.contasapagar.infrastructure.web;

import br.com.eduardoneto.contasapagar.domain.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccountNotFoundException.class)
    public ProblemDetail handleAccountNotFound(AccountNotFoundException ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        pd.setType(URI.create("/errors/account-not-found"));
        return pd;
    }

    @ExceptionHandler(SupplierNotFoundException.class)
    public ProblemDetail handleSupplierNotFound(SupplierNotFoundException ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        pd.setType(URI.create("/errors/supplier-not-found"));
        return pd;
    }

    @ExceptionHandler(AccountAlreadyPaidException.class)
    public ProblemDetail handleAccountAlreadyPaid(AccountAlreadyPaidException ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
        pd.setType(URI.create("/errors/account-already-paid"));
        return pd;
    }

    @ExceptionHandler(AccountCancelledException.class)
    public ProblemDetail handleAccountCancelled(AccountCancelledException ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
        pd.setType(URI.create("/errors/account-cancelled"));
        return pd;
    }

    @ExceptionHandler(InvalidAmountException.class)
    public ProblemDetail handleInvalidAmount(InvalidAmountException ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
        pd.setType(URI.create("/errors/invalid-amount"));
        return pd;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage, (a, b) -> a));
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Erro de validação");
        pd.setType(URI.create("/errors/validation"));
        pd.setProperty("fields", errors);
        return pd;
    }
}
