package org.alopsalv.novabank.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad que registra cada transacción financiera.
 */
@Entity
@Table(name = "movimientos")
public class Movimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Relación Muchos a Uno con Cuenta.
     * Varios movimientos pertenecen a una sola cuenta.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cuenta_id", nullable = false)
    private Cuenta cuenta;

    /**
     * Guarda el Enum como un String en lugar de un índice numérico.
     * Esto hace que la base de datos sea mucho más legible y segura si el Enum cambia.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoMovimiento tipo;

    @Column(nullable = false)
    private BigDecimal cantidad;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fecha;

    public Movimiento() {
    }

    public Movimiento(Cuenta cuenta, TipoMovimiento tipo, BigDecimal cantidad) {
        this.cuenta = cuenta;
        this.tipo = tipo;
        this.cantidad = cantidad;
    }

    /**
     * Asigna la fecha automáticamente justo antes de guardar en base de datos.
     */
    @PrePersist
    protected void onCreate() {
        this.fecha = LocalDateTime.now();
    }

    // --- Getters y Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cuenta getCuenta() {
        return cuenta;
    }

    public void setCuenta(Cuenta cuenta) {
        this.cuenta = cuenta;
    }

    public TipoMovimiento getTipo() {
        return tipo;
    }

    public void setTipo(TipoMovimiento tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "Movimiento{" +
                "id=" + id +
                ", cuentaId=" + (cuenta != null ? cuenta.getId() : "null") +
                ", tipo=" + tipo +
                ", cantidad=" + cantidad +
                ", fecha=" + fecha +
                '}';
    }
}