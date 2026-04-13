package org.alopsalv.novabank.service;

import org.alopsalv.novabank.config.DatabaseConnectionManager;
import org.alopsalv.novabank.model.Cuenta;
import org.alopsalv.novabank.model.Movimiento;
import org.alopsalv.novabank.model.TipoMovimiento;
import org.alopsalv.novabank.repository.CuentaRepository;
import org.alopsalv.novabank.repository.MovimientoRepository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MovimientoService {

    private final MovimientoRepository movimientoRepository;
    private final CuentaRepository cuentaRepository;

    public MovimientoService(MovimientoRepository movimientoRepository, CuentaRepository cuentaRepository) {
        this.movimientoRepository = movimientoRepository;
        this.cuentaRepository = cuentaRepository;
    }

    public void registrarMovimiento(Movimiento movimiento, Cuenta cuenta) {
        if (movimiento.getCantidad().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
        }

        if (movimiento.getTipo() == TipoMovimiento.DEPOSITO || movimiento.getTipo() == TipoMovimiento.TRANSFERENCIA_ENTRANTE) {
            cuenta.setSaldo(cuenta.getSaldo().add(movimiento.getCantidad()));
        } else if (movimiento.getTipo() == TipoMovimiento.RETIRO || movimiento.getTipo() == TipoMovimiento.TRANSFERENCIA_SALIENTE) {
            if (cuenta.getSaldo().compareTo(movimiento.getCantidad()) < 0) {
                throw new IllegalArgumentException("Saldo insuficiente.");
            }
            cuenta.setSaldo(cuenta.getSaldo().subtract(movimiento.getCantidad()));
        }

        //Actualizamos el saldo en la base de datos
        cuentaRepository.actualizarSaldo(cuenta.getId(), cuenta.getSaldo());
        movimientoRepository.guardar(movimiento);
    }

    //MÉTODO TRANSACCIONAL
    public void transferir(String ibanOrigen, String ibanDestino, BigDecimal cantidad) {
        if (cantidad.compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("Cantidad inválida.");

        //Iniciamos la conexión única para toda la operación
        try (Connection conn = DatabaseConnectionManager.getConnection()) {
            //Desactivamos el autocommit para controlar nosotros el final
            conn.setAutoCommit(false);

            try {
                //Buscamos las cuentas dentro de esta conexión
                Cuenta origen = cuentaRepository.buscarPorNumero(ibanOrigen, conn)
                        .orElseThrow(() -> new IllegalArgumentException("Cuenta origen no encontrada."));
                Cuenta destino = cuentaRepository.buscarPorNumero(ibanDestino, conn)
                        .orElseThrow(() -> new IllegalArgumentException("Cuenta destino no encontrada."));

                if (origen.getSaldo().compareTo(cantidad) < 0) throw new IllegalArgumentException("Saldo insuficiente.");

                //Actualizamos saldos en la BD
                cuentaRepository.actualizarSaldo(origen.getId(), origen.getSaldo().subtract(cantidad), conn);
                cuentaRepository.actualizarSaldo(destino.getId(), destino.getSaldo().add(cantidad), conn);

                //Registramos los movimientos
                registrarMovimientoTransaccional(origen.getId(), TipoMovimiento.TRANSFERENCIA_SALIENTE, cantidad, conn);
                registrarMovimientoTransaccional(destino.getId(), TipoMovimiento.TRANSFERENCIA_ENTRANTE, cantidad, conn);

                //Si todo ha ido bien, confirmamos los cambios
                conn.commit();

            } catch (Exception e) {
                //Si algo falla, deshacemos todo
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error de base de datos en la transferencia", e);
        }
    }

    //Método auxiliar para no repetir código en la transacción
    private void registrarMovimientoTransaccional(Long cuentaId, TipoMovimiento tipo, BigDecimal cant, Connection conn) {
        Movimiento m = new Movimiento();
        m.setCuentaId(cuentaId);
        m.setTipo(tipo);
        m.setCantidad(cant);
        m.setFecha(LocalDateTime.now());
        movimientoRepository.guardar(m, conn);
    }

    public List<Movimiento> obtenerMovimientosDeCuenta(Long id) { return movimientoRepository.buscarPorCuentaId(id); }

    public List<Movimiento> obtenerMovimientosPorFecha(Long id, LocalDate ini, LocalDate fin) {
        LocalDateTime dIni = ini.atStartOfDay();
        LocalDateTime dFin = fin.atTime(23, 59, 59);
        return movimientoRepository.buscarPorCuentaId(id).stream()
                .filter(m -> !m.getFecha().isBefore(dIni) && !m.getFecha().isAfter(dFin))
                .sorted(Comparator.comparing(Movimiento::getFecha).reversed())
                .collect(Collectors.toList());
    }
}