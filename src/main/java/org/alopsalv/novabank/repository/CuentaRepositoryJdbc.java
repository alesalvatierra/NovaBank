package org.alopsalv.novabank.repository;

import org.alopsalv.novabank.config.DatabaseConnectionManager;
import org.alopsalv.novabank.model.Cuenta;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CuentaRepositoryJdbc implements CuentaRepository {

    @Override
    public Cuenta guardar(Cuenta cuenta) {
        String sql = "INSERT INTO cuentas (numero_cuenta, cliente_id, saldo) VALUES (?, ?, ?) RETURNING id, fecha_creacion";
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cuenta.getNumeroCuenta());
            stmt.setLong(2, cuenta.getClienteId());
            stmt.setBigDecimal(3, cuenta.getSaldo());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                cuenta.setId(rs.getLong("id"));
            }
            return cuenta;
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar la cuenta", e);
        }
    }

    @Override
    public Optional<Cuenta> buscarPorId(Long id) {
        String sql = "SELECT * FROM cuentas WHERE id = ?";
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapearCuenta(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar cuenta por ID", e);
        }
    }

    @Override
    public Optional<Cuenta> buscarPorNumero(String numeroCuenta) {
        try (Connection conn = DatabaseConnectionManager.getConnection()) {
            return buscarPorNumero(numeroCuenta, conn);
        } catch (SQLException e) {
            throw new RuntimeException("Error de conexión al buscar cuenta por número", e);
        }
    }

    //VARIANTE TRANSACCIONAL
    @Override
    public Optional<Cuenta> buscarPorNumero(String numeroCuenta, Connection conn) {
        String sql = "SELECT * FROM cuentas WHERE numero_cuenta = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, numeroCuenta);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapearCuenta(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar cuenta por número: " + numeroCuenta, e);
        }
    }

    @Override
    public List<Cuenta> buscarPorClienteId(Long clienteId) {
        List<Cuenta> cuentas = new ArrayList<>();
        String sql = "SELECT * FROM cuentas WHERE cliente_id = ?";
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, clienteId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                cuentas.add(mapearCuenta(rs));
            }
            return cuentas;
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar cuentas del cliente: " + clienteId, e);
        }
    }

    @Override
    public Cuenta actualizarSaldo(Long cuentaId, BigDecimal nuevoSaldo) {
        try (Connection conn = DatabaseConnectionManager.getConnection()) {
            return actualizarSaldo(cuentaId, nuevoSaldo, conn);
        } catch (SQLException e) {
            throw new RuntimeException("Error de conexión al actualizar saldo", e);
        }
    }

    //VARIANTE TRANSACCIONAL
    @Override
    public Cuenta actualizarSaldo(Long cuentaId, BigDecimal nuevoSaldo, Connection conn) {
        String sql = "UPDATE cuentas SET saldo = ? WHERE id = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setBigDecimal(1, nuevoSaldo);
            stmt.setLong(2, cuentaId);
            stmt.executeUpdate();

            //Devolvemos la cuenta actualizada buscándola por ID
            return buscarPorId(cuentaId).orElseThrow(() -> new RuntimeException("Cuenta no encontrada tras actualizar saldo"));
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar el saldo de la cuenta: " + cuentaId, e);
        }
    }

    private Cuenta mapearCuenta(ResultSet rs) throws SQLException {
        Cuenta c = new Cuenta();
        c.setId(rs.getLong("id"));
        c.setNumeroCuenta(rs.getString("numero_cuenta"));
        c.setClienteId(rs.getLong("cliente_id"));
        c.setSaldo(rs.getBigDecimal("saldo"));
        return c;
    }
}