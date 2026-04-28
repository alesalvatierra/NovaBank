package org.alopsalv.novabank.exception;

/**
 * Excepción personalizada para errores de saldo en operaciones bancarias.
 */
public class SaldoInsuficienteException extends RuntimeException {
    public SaldoInsuficienteException(String mensaje) {
        super(mensaje);
    }
}