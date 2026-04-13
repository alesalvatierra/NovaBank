package org.alopsalv.novabank.repository;

import org.alopsalv.novabank.config.DatabaseConnectionManager;
import org.alopsalv.novabank.model.Movimiento;
import org.alopsalv.novabank.model.TipoMovimiento;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MovimientoRepositoryJdbc implements MovimientoRepository {

    @Override
    public Movimiento guardar(Movimiento movimiento) {
        try (Connection conn = DatabaseConnectionManager.getConnection()) {
            return guardar(movimiento, conn);
        } catch (SQLException e) {
            throw new RuntimeException("Error de conexión al guardar movimiento", e);
        }
    }

    //VARIANTE TRANSACCIONAL
    @Override
    public Movimiento guardar(Movimiento movimiento, Connection conn) {
        String sql = "INSERT INTO movimientos (cuenta_id, tipo, cantidad) VALUES (?, ?, ?) RETURNING id, fecha";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setLong(1, movimiento.getCuentaId());
            stmt.setString(2, movimiento.getTipo().toString());
            stmt.setBigDecimal(3, movimiento.getCantidad());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                movimiento.setId(rs.getLong("id"));
                movimiento.setFecha(rs.getTimestamp("fecha").toLocalDateTime());
            }
            return movimiento;
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar el movimiento", e);
        }
    }

    @Override
    public List<Movimiento> buscarPorCuentaId(Long cuentaId) {
        List<Movimiento> movimientos = new ArrayList<>();
        String sql = "SELECT * FROM movimientos WHERE cuenta_id = ?";
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, cuentaId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                movimientos.add(mapearMovimiento(rs));
            }
            return movimientos;
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar movimientos de la cuenta: " + cuentaId, e);
        }
    }

    @Override
    public List<Movimiento> buscarPorCuentaIdYFechas(Long cuentaId, LocalDateTime inicio, LocalDateTime fin) {
        List<Movimiento> movimientos = new ArrayList<>();
        String sql = "SELECT * FROM movimientos WHERE cuenta_id = ? AND fecha >= ? AND fecha <= ?";
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, cuentaId);
            stmt.setTimestamp(2, Timestamp.valueOf(inicio));
            stmt.setTimestamp(3, Timestamp.valueOf(fin));

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                movimientos.add(mapearMovimiento(rs));
            }
            return movimientos;
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar movimientos por fechas", e);
        }
    }

    private Movimiento mapearMovimiento(ResultSet rs) throws SQLException {
        Movimiento m = new Movimiento();
        m.setId(rs.getLong("id"));
        m.setCuentaId(rs.getLong("cuenta_id"));
        m.setCantidad(rs.getBigDecimal("cantidad"));
        m.setTipo(TipoMovimiento.valueOf(rs.getString("tipo")));
        m.setFecha(rs.getTimestamp("fecha").toLocalDateTime());

        return m;
    }
}