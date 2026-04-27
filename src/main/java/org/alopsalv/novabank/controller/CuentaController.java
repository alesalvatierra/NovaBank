package org.alopsalv.novabank.controller;

import jakarta.validation.Valid;
import org.alopsalv.novabank.dto.CuentaDTO;
import org.alopsalv.novabank.dto.CuentaMapper;
import org.alopsalv.novabank.dto.OperacionDTO;
import org.alopsalv.novabank.model.Cuenta;
import org.alopsalv.novabank.model.TipoMovimiento;
import org.alopsalv.novabank.service.CuentaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador REST para la gestión de cuentas y operaciones financieras.
 */
@RestController
@RequestMapping("/api/v1/cuentas")
public class CuentaController {

    private final CuentaService cuentaService;

    public CuentaController(CuentaService cuentaService) {
        this.cuentaService = cuentaService;
    }

    /**
     * Obtiene la lista de todas las cuentas del sistema.
     */
    @GetMapping
    public ResponseEntity<List<CuentaDTO>> listarCuentas() {
        List<CuentaDTO> cuentas = cuentaService.listarCuentas().stream()
                .map(CuentaMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(cuentas);
    }

    /**
     * Obtiene los detalles de una cuenta específica.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CuentaDTO> obtenerCuenta(@PathVariable Long id) {
        Cuenta cuenta = cuentaService.obtenerCuenta(id);
        return ResponseEntity.ok(CuentaMapper.toDTO(cuenta));
    }
}