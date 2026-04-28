package org.alopsalv.novabank.service;

import org.alopsalv.novabank.dto.CuentaDTO;
import org.alopsalv.novabank.dto.CuentaMapper;
import org.alopsalv.novabank.dto.OperacionDTO;
import org.alopsalv.novabank.model.Cuenta;
import org.alopsalv.novabank.model.Movimiento;
import org.alopsalv.novabank.model.TipoMovimiento;
import org.alopsalv.novabank.repository.CuentaRepository;
import org.alopsalv.novabank.repository.MovimientoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OperacionService {

    private final CuentaRepository cuentaRepository;
    private final MovimientoRepository movimientoRepository;

    public OperacionService(CuentaRepository cuentaRepository, MovimientoRepository movimientoRepository) {
        this.cuentaRepository = cuentaRepository;
        this.movimientoRepository = movimientoRepository;
    }

    public CuentaDTO realizarOperacion(OperacionDTO operacionDTO, TipoMovimiento tipo) {
        Cuenta cuenta = cuentaRepository.findByNumeroCuenta(operacionDTO.getNumeroCuenta())
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada: " + operacionDTO.getNumeroCuenta()));

        if (tipo == TipoMovimiento.RETIRO && cuenta.getSaldo().compareTo(operacionDTO.getImporte()) < 0) {
            throw new org.alopsalv.novabank.exception.SaldoInsuficienteException("Saldo insuficiente para realizar el retiro");
        }

        if (tipo == TipoMovimiento.DEPOSITO) {
            cuenta.setSaldo(cuenta.getSaldo().add(operacionDTO.getImporte()));
        } else {
            cuenta.setSaldo(cuenta.getSaldo().subtract(operacionDTO.getImporte()));
        }

        Movimiento movimiento = new Movimiento(cuenta, tipo, operacionDTO.getImporte());
        movimientoRepository.save(movimiento);

        return org.alopsalv.novabank.dto.CuentaMapper.toDTO(cuentaRepository.save(cuenta));
    }

}