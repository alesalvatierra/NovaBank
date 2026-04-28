package org.alopsalv.novabank.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * DTO para capturar los datos de una operación
 */
public class OperacionDTO {

    @NotBlank(message = "El número de cuenta es obligatorio")
    private String numeroCuenta;

    @NotNull(message = "El importe es obligatorio")
    @DecimalMin(value = "0.01", message = "El importe debe ser al menos 0.01")
    private BigDecimal importe;

    private String descripcion;

    public OperacionDTO() {}

    public OperacionDTO(String numeroCuenta, BigDecimal importe, String descripcion) {
        this.numeroCuenta = numeroCuenta;
        this.importe = importe;
        this.descripcion = descripcion;
    }

    // Getters y Setters
    public String getNumeroCuenta() { return numeroCuenta; }
    public void setNumeroCuenta(String numeroCuenta) { this.numeroCuenta = numeroCuenta; }
    public BigDecimal getImporte() { return importe; }
    public void setImporte(BigDecimal importe) { this.importe = importe; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}
