package org.alopsalv.novabank.dto;

public class LoginResponseDTO {

    private String token;
    private String tipo = "Bearer";
    private Long expiracion;

    public LoginResponseDTO() {}

    public LoginResponseDTO(String token, Long expiracion) {
        this.token = token;
        this.expiracion = expiracion;
    }

    // Getters y Setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public Long getExpiracion() { return expiracion; }
    public void setExpiracion(Long expiracion) { this.expiracion = expiracion; }
}