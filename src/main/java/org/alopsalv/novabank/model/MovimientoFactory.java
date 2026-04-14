package org.alopsalv.novabank.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MovimientoFactory {

    public static Movimiento crearDeposito(Long cuentaId, BigDecimal monto) {
        Movimiento m = new Movimiento();
        m.setCuentaId(cuentaId);
        m.setTipo(TipoMovimiento.DEPOSITO);
        m.setCantidad(monto);
        m.setFecha(LocalDateTime.now());
        return m;
    }

    public static Movimiento crearRetiro(Long cuentaId, BigDecimal monto) {
        Movimiento m = new Movimiento();
        m.setCuentaId(cuentaId);
        m.setTipo(TipoMovimiento.RETIRO);
        m.setCantidad(monto);
        m.setFecha(LocalDateTime.now());
        return m;
    }

    public static Movimiento crearTransferenciaSaliente(Long cuentaId, BigDecimal monto) {
        Movimiento m = new Movimiento();
        m.setCuentaId(cuentaId);
        m.setTipo(TipoMovimiento.TRANSFERENCIA_SALIENTE);
        m.setCantidad(monto);
        m.setFecha(LocalDateTime.now());
        return m;
    }

    public static Movimiento crearTransferenciaEntrante(Long cuentaId, BigDecimal monto) {
        Movimiento m = new Movimiento();
        m.setCuentaId(cuentaId);
        m.setTipo(TipoMovimiento.TRANSFERENCIA_ENTRANTE);
        m.setCantidad(monto);
        m.setFecha(LocalDateTime.now());
        return m;
    }
}