package org.alopsalv.novabank.dto;

import org.alopsalv.novabank.model.Cuenta;

public class CuentaMapper {

    public static CuentaDTO toDTO(Cuenta cuenta) {
        if (cuenta == null) return null;

        return new CuentaDTO(
                cuenta.getId(),
                cuenta.getNumeroCuenta(),
                cuenta.getSaldo(),
                cuenta.getFechaCreacion(),
                cuenta.getCliente() != null ? cuenta.getCliente().getId() : null
        );
    }
}