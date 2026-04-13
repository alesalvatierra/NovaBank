package org.alopsalv.novabank.repository;

import org.alopsalv.novabank.model.Cliente;
import java.util.List;
import java.util.Optional;

public interface ClienteRepository {
    Cliente guardar(Cliente cliente);
    Optional<Cliente> buscarPorId(Long id);
    Optional<Cliente> buscarPorDni(String dni);
    List<Cliente> listarTodos();
    void eliminar(Long id);
}