package org.alopsalv.novabank.repository;

import org.alopsalv.novabank.model.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CuentaRepository extends JpaRepository<Cuenta, Long> {

    // Spring hace la query SQL automáticamente solo con leer el nombre del método
    Optional<Cuenta> findByNumeroCuenta(String numeroCuenta);

    // Busca todas las cuentas de un cliente por su ID
    List<Cuenta> findByClienteId(Long clienteId);

    // Carga cuentas con sus movimientos en una sola consulta (evita el problema N+1)
    @Query("SELECT c FROM Cuenta c LEFT JOIN FETCH c.movimientos WHERE c.cliente.id = :clienteId")
    List<Cuenta> findByClienteIdWithMovimientos(@Param("clienteId") Long clienteId);
}