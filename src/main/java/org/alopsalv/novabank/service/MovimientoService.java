package org.alopsalv.novabank.service;

import org.alopsalv.novabank.dto.MovimientoDTO;
import org.alopsalv.novabank.dto.MovimientoMapper;
import org.alopsalv.novabank.repository.MovimientoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MovimientoService {

    private final MovimientoRepository movimientoRepository;

    public MovimientoService(MovimientoRepository movimientoRepository) {
        this.movimientoRepository = movimientoRepository;
    }

    /**
     * Obtiene el historial de una cuenta.
     */
    @Transactional(readOnly = true)
    public List<MovimientoDTO> obtenerHistorialPorCuenta(Long cuentaId) {
        return movimientoRepository.findByCuentaIdOrderByFechaDesc(cuentaId)
                .stream()
                .map(MovimientoMapper::toDTO)
                .collect(Collectors.toList());
    }
}