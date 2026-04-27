package org.alopsalv.novabank.controller;

import jakarta.validation.Valid;
import org.alopsalv.novabank.service.JwtService;
import org.alopsalv.novabank.dto.LoginRequestDTO;
import org.alopsalv.novabank.dto.LoginResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    // Atributos
    private final JwtService jwtService;

    // Constructor
    public AuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    // Métodos
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {

        if (request.getUsername() != null && request.getPassword() != null) {
            String tokenGenerado = jwtService.generarToken(request.getUsername());

            LoginResponseDTO response = new LoginResponseDTO(
                    tokenGenerado,
                    86400000L
            );

            return ResponseEntity.ok(response);
        }

        return ResponseEntity.status(401).build();
    }
}