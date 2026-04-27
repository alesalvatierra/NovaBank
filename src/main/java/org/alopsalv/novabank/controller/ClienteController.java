package org.alopsalv.novabank.controller;

import jakarta.validation.Valid;
import org.alopsalv.novabank.dto.ClienteDTO;
import org.alopsalv.novabank.service.ClienteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST que expone los endpoints para la gestión de clientes.
 */
@RestController
@RequestMapping("/api/v1/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    // Inyección de dependencias
    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    /**
     * Devuelve la lista de todos los clientes.
     */
    @GetMapping
    public ResponseEntity<List<ClienteDTO>> listarClientes() {
        List<ClienteDTO> clientes = clienteService.listarClientes();
        return ResponseEntity.ok(clientes);
    }

    /**
     * Busca un cliente por su ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> obtenerCliente(@PathVariable Long id) {
        ClienteDTO cliente = clienteService.obtenerCliente(id);
        return ResponseEntity.ok(cliente);
    }

    /**
     * Crea un nuevo cliente. Usa @Valid para aplicar las reglas del DTO.
     */
    @PostMapping
    public ResponseEntity<ClienteDTO> registrarCliente(@Valid @RequestBody ClienteDTO clienteDTO) {
        ClienteDTO nuevoCliente = clienteService.registrarCliente(clienteDTO);
        return new ResponseEntity<>(nuevoCliente, HttpStatus.CREATED);
    }
}