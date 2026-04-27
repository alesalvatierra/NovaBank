package org.alopsalv.novabank.dto;

import org.alopsalv.novabank.model.Movimiento;

public class MovimientoMapper {

    public static MovimientoDTO toDTO(Movimiento movimiento) {
        if (movimiento == null) return null;

        return new MovimientoDTO(
                movimiento.getId(),
                movimiento.getTipo().name(),
                movimiento.getCantidad(),
                movimiento.getFecha(),
                movimiento.getCuenta() != null ? movimiento.getCuenta().getId() : null
        );
    }
}