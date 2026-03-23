package org.alopsalv.novabank.service;

import org.alopsalv.novabank.model.Cliente;
import org.alopsalv.novabank.repository.ClienteRepository;

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
            throw new IllegalArgumentException("El DNI ya está registrado");
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
}