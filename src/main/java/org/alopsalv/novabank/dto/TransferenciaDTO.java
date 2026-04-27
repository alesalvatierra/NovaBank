package org.alopsalv.novabank.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class TransferenciaDTO {

    @NotNull(message = "La cuenta origen es obligatoria")
    private String cuentaOrigen;

    @NotNull(message = "La cuenta destino es obligatoria")
    private String cuentaDestino;

    @NotNull(message = "El importe es obligatorio")
    @DecimalMin(value = "0.01", message = "El importe debe ser al menos 0.01")
    private BigDecimal importe;

    private String concepto;

    public TransferenciaDTO() {}

    public TransferenciaDTO(String cuentaOrigen, String cuentaDestino, BigDecimal importe, String concepto) {
        this.cuentaOrigen = cuentaOrigen;
        this.cuentaDestino = cuentaDestino;
        this.importe = importe;
        this.concepto = concepto;
    }

    // Getters y Setters
    public String getCuentaOrigen() { return cuentaOrigen; }
    public void setCuentaOrigen(String cuentaOrigen) { this.cuentaOrigen = cuentaOrigen; }
    public String getCuentaDestino() { return cuentaDestino; }
    public void setCuentaDestino(String cuentaDestino) { this.cuentaDestino = cuentaDestino; }
    public BigDecimal getImporte() { return importe; }
    public void setImporte(BigDecimal importe) { this.importe = importe; }
    public String getConcepto() { return concepto; }
    public void setConcepto(String concepto) { this.concepto = concepto; }
}
