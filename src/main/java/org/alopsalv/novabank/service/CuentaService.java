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

import java.util.List;

@Service
@Transactional
public class CuentaService {

    private final CuentaRepository cuentaRepository;
    // Añadimos el repositorio de movimientos
    private final MovimientoRepository movimientoRepository;

    // Actualizamos el constructor para inyectar ambos repositorios
    public CuentaService(CuentaRepository cuentaRepository, MovimientoRepository movimientoRepository) {
        this.cuentaRepository = cuentaRepository;
        this.movimientoRepository = movimientoRepository;
    }

    @Transactional(readOnly = true)
    public List<Cuenta> listarCuentas() {
        return cuentaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Cuenta obtenerCuenta(Long id) {
        return cuentaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada con ID: " + id));
    }

    public Cuenta crearCuenta(Cuenta cuenta) {
        return cuentaRepository.save(cuenta);
    }
}