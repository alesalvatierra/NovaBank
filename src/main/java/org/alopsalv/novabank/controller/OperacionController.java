package org.alopsalv.novabank.controller;

import jakarta.validation.Valid;
import org.alopsalv.novabank.dto.CuentaDTO;
import org.alopsalv.novabank.dto.OperacionDTO;
import org.alopsalv.novabank.model.TipoMovimiento;
import org.alopsalv.novabank.service.OperacionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/operaciones")
@io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth")
public class OperacionController {

    private final OperacionService operacionService;

    public OperacionController(OperacionService operacionService) {
        this.operacionService = operacionService;
    }

    @PostMapping("/deposito")
    public ResponseEntity<CuentaDTO> realizarDeposito(@Valid @RequestBody OperacionDTO operacionDTO) {
        CuentaDTO cuentaActualizada = operacionService.realizarOperacion(operacionDTO, TipoMovimiento.DEPOSITO);
        return ResponseEntity.ok(cuentaActualizada);
    }

    @PostMapping("/retiro")
    public ResponseEntity<CuentaDTO> realizarRetiro(@Valid @RequestBody OperacionDTO operacionDTO) {
        CuentaDTO cuentaActualizada = operacionService.realizarOperacion(operacionDTO, TipoMovimiento.RETIRO);
        return ResponseEntity.ok(cuentaActualizada);
    }
}