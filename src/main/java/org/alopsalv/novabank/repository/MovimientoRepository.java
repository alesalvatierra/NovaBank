package org.alopsalv.novabank.repository;

import org.alopsalv.novabank.model.Movimiento;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;

public interface MovimientoRepository {
    Movimiento guardar(Movimiento movimiento);
    List<Movimiento> buscarPorCuentaId(Long cuentaId);
    List<Movimiento> buscarPorCuentaIdYFechas(Long cuentaId, LocalDateTime inicio, LocalDateTime fin);

    // Variante transaccional: recibe la Connection activa
    Movimiento guardar(Movimiento movimiento, Connection conn);
}