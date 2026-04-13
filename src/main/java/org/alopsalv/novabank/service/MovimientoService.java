package org.alopsalv.novabank.service;

import org.alopsalv.novabank.model.Cuenta;
import org.alopsalv.novabank.model.Movimiento;
import org.alopsalv.novabank.model.TipoMovimiento;
import org.alopsalv.novabank.repository.MovimientoRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator; // Necesario para ordenar
import java.util.List;
import java.util.stream.Collectors; // Necesario para los Streams

public class MovimientoService {

    private final MovimientoRepository movimientoRepository;

    //Constructor
    public MovimientoService(MovimientoRepository movimientoRepository) {
        this.movimientoRepository = movimientoRepository;
    }

    //Método para registrar un movimiento
    public void registrarMovimiento(Movimiento movimiento, Cuenta cuenta) {
        if (movimiento.getCantidad().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("La cantidad del movimiento debe ser mayor que cero.");
        }

        if (movimiento.getTipo() == TipoMovimiento.DEPOSITO || movimiento.getTipo() == TipoMovimiento.TRANSFERENCIA_ENTRANTE) {
            BigDecimal nuevoSaldo = cuenta.getSaldo().add(movimiento.getCantidad());
            cuenta.setSaldo(nuevoSaldo);
        }

        if (movimiento.getTipo() == TipoMovimiento.RETIRO || movimiento.getTipo() == TipoMovimiento.TRANSFERENCIA_SALIENTE) {
            BigDecimal nuevoSaldo = cuenta.getSaldo().subtract(movimiento.getCantidad());
            if (nuevoSaldo.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("Operación denegada: Saldo insuficiente.");
            }
            cuenta.setSaldo(nuevoSaldo);
        }

        movimientoRepository.guardar(movimiento);
    }

    //Método para obtener todos los movimientos de una cuenta concreta
    public List<Movimiento> obtenerMovimientosDeCuenta(Long cuentaId) {
        return movimientoRepository.buscarPorCuentaId(cuentaId);
    }

    //Método para filtrar movimientos por cuenta e intervalo de fechas
    public List<Movimiento> obtenerMovimientosPorFecha(Long cuentaId, LocalDate inicio, LocalDate fin) {
        LocalDateTime fechaInicio = inicio.atStartOfDay();
        LocalDateTime fechaFin = fin.atTime(23, 59, 59);

        //Nos traemos TODOS los movimientos de la cuenta desde la base de datos
        List<Movimiento> todos = movimientoRepository.buscarPorCuentaId(cuentaId);

        //Usamos Streams para filtrar en memoria
        return todos.stream()
                //Descartamos los que sean anteriores a la fecha de inicio
                .filter(m -> !m.getFecha().isBefore(fechaInicio))
                //Descartamos los que sean posteriores a la fecha de fin
                .filter(m -> !m.getFecha().isAfter(fechaFin))
                //Los ordenamos del más reciente al más antiguo
                .sorted(Comparator.comparing(Movimiento::getFecha).reversed())
                .collect(Collectors.toList());
    }
}