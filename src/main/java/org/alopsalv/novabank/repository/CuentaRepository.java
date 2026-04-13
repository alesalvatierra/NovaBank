package org.alopsalv.novabank.repository;

import org.alopsalv.novabank.model.Cuenta;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public interface CuentaRepository {
    Cuenta guardar(Cuenta cuenta);
    Optional<Cuenta> buscarPorId(Long id);
    Optional<Cuenta> buscarPorNumero(String numeroCuenta);
    List<Cuenta> buscarPorClienteId(Long clienteId);
    Cuenta actualizarSaldo(Long cuentaId, BigDecimal nuevoSaldo);

    //Variantes transaccionales
    Optional<Cuenta> buscarPorNumero(String numeroCuenta, Connection conn);
    Cuenta actualizarSaldo(Long cuentaId, BigDecimal nuevoSaldo, Connection conn);
}