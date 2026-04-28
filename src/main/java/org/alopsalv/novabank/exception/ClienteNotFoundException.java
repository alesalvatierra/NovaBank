package org.alopsalv.novabank.exception;

/**
 * Excepción personalizada para cuando no se encuentra un cliente.
 */
public class ClienteNotFoundException extends RuntimeException {
    public ClienteNotFoundException(String mensaje) {
        super(mensaje);
    }
}