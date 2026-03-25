package org.alopsalv.novabank.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Cuenta {
    //Atributos
    private Long id, clienteId;
    private String numeroCuenta;
    private BigDecimal saldo;
    private LocalDateTime fechaCreacion;

    //Constructor
    public Cuenta() {
    }

    //Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    //Método toString
    @Override
    public String toString() {
        return "Cuenta{" +
                "id=" + id +
                ", clienteId=" + clienteId +
                ", numeroCuenta='" + numeroCuenta + '\'' +
                ", saldo=" + saldo +
                ", fechaCreacion=" + fechaCreacion +
                '}';
    }
}
