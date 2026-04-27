package org.alopsalv.novabank.repository;

import org.alopsalv.novabank.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio JPA para la entidad Cliente.
 */
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByDni(String dni);

    Optional<Cliente> findByEmail(String email);

    boolean existsByDni(String dni);

    boolean existsByEmail(String email);

    boolean existsByTelefono(String telefono);
}