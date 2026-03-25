package org.alopsalv.novabank.service;

import org.alopsalv.novabank.model.Cuenta;
import org.alopsalv.novabank.model.Movimiento;
import org.alopsalv.novabank.model.TipoMovimiento;
import org.alopsalv.novabank.repository.MovimientoRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MovimientoService {

    private final MovimientoRepository movimientoRepository;

    //Constructor
    public MovimientoService(MovimientoRepository movimientoRepository) {
        this.movimientoRepository = movimientoRepository;
    }

    //Método para registrar un movimiento
    public void registrarMovimiento(Movimiento movimiento, Cuenta cuenta) {

        //En caso de DEPOSITO, sumamos
        if (movimiento.getTipo() == TipoMovimiento.DEPOSITO || movimiento.getTipo() == TipoMovimiento.TRANSFERENCIA_ENTRANTE) {
            BigDecimal nuevoSaldo = cuenta.getSaldo().add(movimiento.getCantidad());
            cuenta.setSaldo(nuevoSaldo);
        }

        //En caso de RETIRADA, restamos
        if (movimiento.getTipo() == TipoMovimiento.RETIRO|| movimiento.getTipo() == TipoMovimiento.TRANSFERENCIA_SALIENTE) {
            BigDecimal nuevoSaldo = cuenta.getSaldo().subtract(movimiento.getCantidad());
            //Validación para que la cantidad sea mayor de 0
            if (nuevoSaldo.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("Operación denegada: Saldo insuficiente.");
            }
            cuenta.setSaldo(nuevoSaldo);
        }

        //Validación para que la cantidad sea mayor de 0
        if (movimiento.getCantidad().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("La cantidad del movimiento debe ser mayor que cero.");
        }

        movimientoRepository.guardarMovimiento(movimiento);
    }

    // Método para obtener todos los movimientos de una cuenta concreta
    public List<Movimiento> obtenerMovimientosDeCuenta(Long cuentaId) {
        return movimientoRepository.buscarPorCuentaId(cuentaId);
    }

    // Método para filtrar movimientos por cuenta e intervalo de fechas
    public List<Movimiento> obtenerMovimientosPorFecha(Long cuentaId, LocalDate inicio, LocalDate fin) {
        //Obtenemos todos los movimientos
        List<Movimiento> todos = movimientoRepository.buscarPorCuentaId(cuentaId);
        List<Movimiento> filtrados = new ArrayList<>();

        //Convertimos las fechas de inicio y fin
        LocalDateTime fechaInicio = inicio.atStartOfDay();
        LocalDateTime fechaFin = fin.atTime(23, 59, 59);

        //Filtramos
        for (Movimiento m : todos) {
            if ((m.getFecha().isEqual(fechaInicio) || m.getFecha().isAfter(fechaInicio)) &&
                    (m.getFecha().isEqual(fechaFin) || m.getFecha().isBefore(fechaFin))) {
                filtrados.add(m);
            }
        }
        return filtrados;
    }
}
