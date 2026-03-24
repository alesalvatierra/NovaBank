package org.alopsalv.novabank.repository;

import org.alopsalv.novabank.model.Cliente;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClienteRepository {
    //Mapa para almacenar objeto cliente.
    private final Map<Long, Cliente> clientes = new HashMap<>();
    //Contador para el autoincrement.
    private long contadorClientes = 1000L;

    //Método para guardar un Cliente.
    public Cliente guardarCliente(Cliente cliente){
        //Asigno id actual y luego incremento el contador.
        cliente.setId(contadorClientes++);
        //Agregamos al HashMap.
        clientes.put(cliente.getId(), cliente);
        return cliente;
    }

    //Método para bucar un cliente por id.
    public Cliente buscarPorId(Long id){

        return clientes.get(id);
    }

    //Método para buscar un cliente por dni.
    public Cliente buscarPorDni(String dni){
        //Recorremos y comprobamos si coincide el dni.
        for (Cliente cliente : clientes.values()){
            if (cliente.getDni().equalsIgnoreCase(dni)){
                return cliente;
            }
        }
        //Si no encuentra nada, devuelve null.
        return null;
    }

    //Método para almacenar en una lista los clientes registrados
    public List<Cliente> obtenerTodos(){
        return new ArrayList<>(clientes.values());
    }

}
