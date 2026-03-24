package org.alopsalv.novabank.service;

import org.alopsalv.novabank.model.Cliente;
import org.alopsalv.novabank.repository.ClienteRepository;

import java.util.List;

public class ClienteService {

    private final ClienteRepository clienteRepository;

    //Constructor
    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    //Método para crear un Cliente.
    public Cliente crearCliente(Cliente clienteNuevo){
        //Validamos que el DNI no sea existente.
        Cliente clienteExistente = clienteRepository.buscarPorDni(clienteNuevo.getDni());
        if (clienteExistente != null){
            throw new IllegalArgumentException("Ya existe un cliente con el DNI " + clienteExistente.getDni());
        }
        //Validamos que el email contenga el símbolo "@".
        if (!clienteNuevo.getEmail().contains("@")){
            throw new IllegalArgumentException("El formato del email no es válido");
        }

        return clienteRepository.guardarCliente(clienteNuevo);
    }

    public Cliente buscarCliente(Long id){
        return clienteRepository.buscarPorId(id);
    }

    // Método para buscar cliente por DNI
    public Cliente buscarClientePorDni(String dni) {
        return clienteRepository.buscarPorDni(dni);
    }

    // Método para obtener todos los clientes registrados
    public List<Cliente> obtenerTodosLosClientes() {
        return clienteRepository.obtenerTodos();
    }
}