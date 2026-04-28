package org.alopsalv.novabank.service;

import org.alopsalv.novabank.dto.CuentaDTO;
import org.alopsalv.novabank.dto.CuentaMapper;
import org.alopsalv.novabank.exception.ClienteNotFoundException;
import org.alopsalv.novabank.model.Cliente;
import org.alopsalv.novabank.model.Cuenta;
import org.alopsalv.novabank.repository.CuentaRepository;
import org.alopsalv.novabank.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CuentaService {

    private final CuentaRepository cuentaRepository;
    private final ClienteRepository clienteRepository;

    public CuentaService(CuentaRepository cuentaRepository, ClienteRepository clienteRepository) {
        this.cuentaRepository = cuentaRepository;
        this.clienteRepository = clienteRepository;
    }

    /**
     * Devuelve la lista de cuentas convertidas a DTO.
     */
    public List<CuentaDTO> listarCuentas() {
        return cuentaRepository.findAll()
                .stream()
                .map(CuentaMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca una cuenta por ID y la devuelve como DTO.
     */
    public CuentaDTO obtenerCuenta(Long id) {
        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new ClienteNotFoundException("Cuenta no encontrada con ID: " + id));
        return CuentaMapper.toDTO(cuenta);
    }

    public CuentaDTO crearCuenta(Long clienteId) {
        // 1. Buscamos al cliente por ID
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ClienteNotFoundException("No se puede crear cuenta: Cliente no encontrado"));

        // 2. Creamos la nueva entidad Cuenta
        Cuenta nuevaCuenta = new Cuenta();
        nuevaCuenta.setCliente(cliente);
        nuevaCuenta.setSaldo(BigDecimal.ZERO);
        // Generamos un número de cuenta aleatorio para la prueba
        nuevaCuenta.setNumeroCuenta("ES" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());

        // 3. Guardamos y devolvemos el DTO
        return CuentaMapper.toDTO(cuentaRepository.save(nuevaCuenta));
    }
}
