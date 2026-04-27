package org.alopsalv.novabank.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    // Atributos de clase JwtService
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    // Métodos

    public String generarToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(obtenerFirma(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key obtenerFirma() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Extrae el nombre de usuario (subject) del token JWT.
     */
    public String extraerUsername(String token) {
        return io.jsonwebtoken.Jwts.parserBuilder()
                .setSigningKey(obtenerFirma())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Valida que el token tenga la firma correcta y no esté caducado.
     */
    public boolean validarToken(String token) {
        try {
            io.jsonwebtoken.Jwts.parserBuilder()
                    .setSigningKey(obtenerFirma())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false; // Si falla la firma o está caducado, devuelve false
        }
    }
}