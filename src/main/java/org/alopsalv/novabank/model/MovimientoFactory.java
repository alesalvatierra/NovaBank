package org.alopsalv.novabank.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
/*
    Clase que implementa el patrón de diseño Factory para centralizar la creación de movimientos
    Clasifica y construye objetos Movimiento según su tipo de forma estandarizada
 */
public class MovimientoFactory {

    //Método crearDeposito
    public static Movimiento crearDeposito(Long cuentaId, BigDecimal monto) {
        Movimiento m = new Movimiento();
        m.setCuentaId(cuentaId);
        m.setTipo(TipoMovimiento.DEPOSITO);
        m.setCantidad(monto);
        m.setFecha(LocalDateTime.now());
        return m;
    }
    //Método crearRetiro
    public static Movimiento crearRetiro(Long cuentaId, BigDecimal monto) {
        Movimiento m = new Movimiento();
        m.setCuentaId(cuentaId);
        m.setTipo(TipoMovimiento.RETIRO);
        m.setCantidad(monto);
        m.setFecha(LocalDateTime.now());
        return m;
    }
    //Método crearTransferenciaSaliente
    public static Movimiento crearTransferenciaSaliente(Long cuentaId, BigDecimal monto) {
        Movimiento m = new Movimiento();
        m.setCuentaId(cuentaId);
        m.setTipo(TipoMovimiento.TRANSFERENCIA_SALIENTE);
        m.setCantidad(monto);
        m.setFecha(LocalDateTime.now());
        return m;
    }
    //Método crearTransferenciaEntrante
    public static Movimiento crearTransferenciaEntrante(Long cuentaId, BigDecimal monto) {
        Movimiento m = new Movimiento();
        m.setCuentaId(cuentaId);
        m.setTipo(TipoMovimiento.TRANSFERENCIA_ENTRANTE);
        m.setCantidad(monto);
        m.setFecha(LocalDateTime.now());
        return m;
    }
}