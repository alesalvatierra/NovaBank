package org.alopsalv.novabank.repository;

import org.alopsalv.novabank.config.DatabaseConnectionManager;
import org.alopsalv.novabank.model.Cliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClienteRepositoryJdbc implements ClienteRepository {

    @Override
    public Cliente guardar(Cliente cliente) {
        // Usamos RETURNING id para que PostgreSQL nos devuelva el ID autogenerado
        String sql = "INSERT INTO clientes (nombre, apellidos, dni, email, telefono) VALUES (?, ?, ?, ?, ?) RETURNING id, fecha_creacion";

        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cliente.getNombre());
            stmt.setString(2, cliente.getApellidos());
            stmt.setString(3, cliente.getDni());
            stmt.setString(4, cliente.getEmail());
            stmt.setString(5, cliente.getTelefono());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                cliente.setId(rs.getLong("id"));
                // Si tienes fechaCreacion en tu modelo Cliente, puedes setearla aquí también
                // cliente.setFechaCreacion(rs.getTimestamp("fecha_creacion").toLocalDateTime());
            }
            return cliente;

        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar el cliente en la base de datos", e);
        }
    }

    @Override
    public Optional<Cliente> buscarPorId(Long id) {
        String sql = "SELECT * FROM clientes WHERE id = ?";
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapearCliente(rs));
            }
            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar cliente por ID: " + id, e);
        }
    }

    @Override
    public Optional<Cliente> buscarPorDni(String dni) {
        String sql = "SELECT * FROM clientes WHERE dni = ?";
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, dni);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapearCliente(rs));
            }
            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar cliente por DNI: " + dni, e);
        }
    }

    @Override
    public List<Cliente> listarTodos() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM clientes";

        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                clientes.add(mapearCliente(rs));
            }
            return clientes;

        } catch (SQLException e) {
            throw new RuntimeException("Error al listar todos los clientes", e);
        }
    }

    @Override
    public void eliminar(Long id) {
        String sql = "DELETE FROM clientes WHERE id = ?";
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar el cliente con ID: " + id, e);
        }
    }

    // Método auxiliar (Mapeo de ResultSet a Objeto Java)
    private Cliente mapearCliente(ResultSet rs) throws SQLException {
        Cliente c = new Cliente();
        c.setId(rs.getLong("id"));
        c.setNombre(rs.getString("nombre"));
        c.setApellidos(rs.getString("apellidos"));
        c.setDni(rs.getString("dni"));
        c.setEmail(rs.getString("email"));
        c.setTelefono(rs.getString("telefono"));
        // Si tienes la fecha, descomenta esto:
        // c.setFechaCreacion(rs.getTimestamp("fecha_creacion").toLocalDateTime());
        return c;
    }
}