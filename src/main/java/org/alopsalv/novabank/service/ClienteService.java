package org.alopsalv.novabank.service;

import org.alopsalv.novabank.model.Cliente;
import org.alopsalv.novabank.repository.ClienteRepository;

import java.util.List;

public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente crearCliente(Cliente c) {
        //Validamos que no haya Null ni en Blanco
        if (c.getNombre() == null || c.getNombre().trim().isEmpty()) throw new IllegalArgumentException("El nombre es obligatorio.");
        if (c.getApellidos() == null || c.getApellidos().trim().isEmpty()) throw new IllegalArgumentException("Los apellidos son obligatorios.");
        if (c.getDni() == null || c.getDni().trim().isEmpty()) throw new IllegalArgumentException("El DNI es obligatorio.");
        if (c.getEmail() == null || c.getEmail().trim().isEmpty()) throw new IllegalArgumentException("El email es obligatorio.");
        if (c.getTelefono() == null || c.getTelefono().trim().isEmpty()) throw new IllegalArgumentException("El teléfono es obligatorio.");

        //Validamos las longitudes según las tablas
        if (c.getNombre().length() > 100) throw new IllegalArgumentException("El nombre no puede exceder 100 caracteres.");
        if (c.getApellidos().length() > 150) throw new IllegalArgumentException("Los apellidos no pueden exceder 150 caracteres.");
        if (c.getDni().length() > 20) throw new IllegalArgumentException("El DNI no puede exceder 20 caracteres.");
        if (c.getEmail().length() > 150) throw new IllegalArgumentException("El email no puede exceder 150 caracteres.");
        if (c.getTelefono().length() > 20) throw new IllegalArgumentException("El teléfono no puede exceder 20 caracteres.");

        //Validamos formato DNI
        if (!c.getDni().matches("^[0-9]{8}[a-zA-Z]$")) {
            throw new IllegalArgumentException("Formato de DNI inválido. Debe tener 8 números y 1 letra.");
        }

        //Validamos formato Email
        if (!c.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("El formato del email no es válido.");
        }

        //Validamos formato de Teléfono
        if (!c.getTelefono().matches("^(\\+34)?[0-9]{9}$")) {
            throw new IllegalArgumentException("Formato de teléfono inválido. Debe tener 9 dígitos (puede incluir +34).");
        }

        //Validamos los campos únicos
        List<Cliente> todos = clienteRepository.obtenerTodos();
        for (Cliente existente : todos) {
            if (existente.getDni().equalsIgnoreCase(c.getDni())) throw new IllegalArgumentException("Ya existe un cliente con el DNI: " + c.getDni());
            if (existente.getEmail().equalsIgnoreCase(c.getEmail())) throw new IllegalArgumentException("Ya existe un cliente con el email: " + c.getEmail());
            if (existente.getTelefono().equals(c.getTelefono())) throw new IllegalArgumentException("Ya existe un cliente con el teléfono: " + c.getTelefono());
        }

        return clienteRepository.guardarCliente(c);
    }

    public Cliente buscarCliente(Long id) {
        return clienteRepository.buscarPorId(id);
    }

    public Cliente buscarClientePorDni(String dni) {
        return clienteRepository.buscarPorDni(dni);
    }

    public List<Cliente> obtenerTodosLosClientes() {
        return clienteRepository.obtenerTodos();
    }
}