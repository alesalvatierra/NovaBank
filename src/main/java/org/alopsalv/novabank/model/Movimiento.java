package org.alopsalv.novabank.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Movimiento {

    private Long id, cuentaId;
    private String tipo;
    private BigDecimal cantidad;
    private LocalDateTime fecha;

    //Constructor
    public Movimiento() {
    }

    //Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCuentaId() {
        return cuentaId;
    }

    public void setCuentaId(Long cuentaId) {
        this.cuentaId = cuentaId;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
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

    //Método toString
    @Override
    public String toString() {
        return "Movimiento{" +
                "id=" + id +
                ", cuentaId=" + cuentaId +
                ", tipo='" + tipo + '\'' +
                ", cantidad=" + cantidad +
                ", fecha=" + fecha +
                '}';
    }
}
