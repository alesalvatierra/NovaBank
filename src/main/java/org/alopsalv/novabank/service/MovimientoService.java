package org.alopsalv.novabank.service;

import org.alopsalv.novabank.model.Cuenta;
import org.alopsalv.novabank.model.Movimiento;
import org.alopsalv.novabank.repository.MovimientoRepository;

import java.math.BigDecimal;

public class MovimientoService {

    private final MovimientoRepository movimientoRepository;

    //Constructor
    public MovimientoService(MovimientoRepository movimientoRepository) {
        this.movimientoRepository = movimientoRepository;
    }

    //Método para registrar un movimiento
    public void registrarMovimiento(Movimiento movimiento, Cuenta cuenta) {

        //En caso de DEPOSITO, sumamos
        if (movimiento.getTipo().equals("DEPOSITO") || movimiento.getTipo().equals("TRANSFERENCIA_ENTRANTE")) {
            BigDecimal nuevoSaldo = cuenta.getSaldo().add(movimiento.getCantidad());
            cuenta.setSaldo(nuevoSaldo);
        }

        //En caso de RETIRADA, restamos
        if (movimiento.getTipo().equals("RETIRADA") || movimiento.getTipo().equals("TRANSFERENCIA_SALIENTE")) {
            BigDecimal nuevoSaldo = cuenta.getSaldo().subtract(movimiento.getCantidad());
            cuenta.setSaldo(nuevoSaldo);
        }
        movimientoRepository.guardarMovimiento(movimiento);
    }

    // Método para obtener todos los movimientos de una cuenta concreta
    public java.util.List<Movimiento> obtenerMovimientosDeCuenta(Long cuentaId) {
        return movimientoRepository.buscarPorCuentaId(cuentaId);
    }
}
