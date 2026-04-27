package org.alopsalv.novabank.service;

import org.alopsalv.novabank.dto.ClienteDTO;
import org.alopsalv.novabank.dto.ClienteMapper;
import org.alopsalv.novabank.model.Cliente;
import org.alopsalv.novabank.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    /**
     * Registra un nuevo cliente validando que el DNI y el Email no existan previamente.
     */
    public ClienteDTO registrarCliente(ClienteDTO clienteDTO) {

        if (clienteRepository.existsByDni(clienteDTO.getDni())) {
            throw new RuntimeException("Ya existe un cliente con este DNI");
        }
        if (clienteRepository.existsByEmail(clienteDTO.getEmail())) {
            throw new RuntimeException("Ya existe un cliente con este Email");
        }

        //Convertimos el DTO que entra a Entidad para la BD
        Cliente cliente = ClienteMapper.toEntity(clienteDTO);

        //Guardamos en BD
        Cliente clienteGuardado = clienteRepository.save(cliente);

        //Devolvemos el resultado convertido de nuevo a DTO
        return ClienteMapper.toDTO(clienteGuardado);
    }

    @Transactional(readOnly = true)
    public ClienteDTO obtenerCliente(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + id));
        return ClienteMapper.toDTO(cliente);
    }

    @Transactional(readOnly = true)
    public List<ClienteDTO> listarClientes() {
        return clienteRepository.findAll().stream()
                .map(ClienteMapper::toDTO)
                .collect(Collectors.toList());
    }
}