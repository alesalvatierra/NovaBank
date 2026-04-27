package org.alopsalv.novabank.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CuentaDTO {
    private Long id;
    private String numeroCuenta;
    private BigDecimal saldo;
    private LocalDateTime fechaCreacion;
    private Long clienteId;

    public CuentaDTO() {}

    public CuentaDTO(Long id, String numeroCuenta, BigDecimal saldo, LocalDateTime fechaCreacion, Long clienteId) {
        this.id = id;
        this.numeroCuenta = numeroCuenta;
        this.saldo = saldo;
        this.fechaCreacion = fechaCreacion;
        this.clienteId = clienteId;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNumeroCuenta() { return numeroCuenta; }
    public void setNumeroCuenta(String numeroCuenta) { this.numeroCuenta = numeroCuenta; }
    public BigDecimal getSaldo() { return saldo; }
    public void setSaldo(BigDecimal saldo) { this.saldo = saldo; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
}
