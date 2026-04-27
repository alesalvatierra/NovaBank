package org.alopsalv.novabank.repository;

import org.alopsalv.novabank.model.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio JPA para la entidad Movimiento.
 */
@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {

    //Obtiene todos los movimientos de una cuenta ordenados del más reciente al más antiguo
    List<Movimiento> findByCuentaIdOrderByFechaDesc(Long cuentaId);

    //Filtra los movimientos de una cuenta en un rango de fechas y los ordena
    List<Movimiento> findByCuentaIdAndFechaBetweenOrderByFechaDesc(Long cuentaId, LocalDateTime inicio, LocalDateTime fin);
}