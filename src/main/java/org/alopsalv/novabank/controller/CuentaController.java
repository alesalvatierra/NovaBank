package org.alopsalv.novabank.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.alopsalv.novabank.dto.CuentaDTO;
import org.alopsalv.novabank.service.CuentaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cuentas")
@SecurityRequirement(name = "bearerAuth")
public class CuentaController {

    private final CuentaService cuentaService;

    public CuentaController(CuentaService cuentaService) {
        this.cuentaService = cuentaService;
    }

    @GetMapping
    public ResponseEntity<List<CuentaDTO>> listarCuentas() {
        return ResponseEntity.ok(cuentaService.listarCuentas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CuentaDTO> obtenerCuenta(@PathVariable Long id) {
        return ResponseEntity.ok(cuentaService.obtenerCuenta(id));
    }

    @PostMapping("/cliente/{clienteId}")
    public ResponseEntity<CuentaDTO> crearCuenta(@PathVariable Long clienteId) {
        return ResponseEntity.ok(cuentaService.crearCuenta(clienteId));
    }
}
