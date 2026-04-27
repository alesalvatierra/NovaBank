package org.alopsalv.novabank.dto;

import org.alopsalv.novabank.model.Cliente;

/**
 * Clase utilitaria para mapear entre la entidad Cliente y su DTO correspondiente.
 */
public class ClienteMapper {

    /**
     * Convierte una entidad Cliente a ClienteDTO para enviarlo en la respuesta HTTP.
     */
    public static ClienteDTO toDTO(Cliente cliente) {
        if (cliente == null) return null;

        return new ClienteDTO(
                cliente.getId(),
                cliente.getNombre(),
                cliente.getApellidos(),
                cliente.getDni(),
                cliente.getEmail(),
                cliente.getTelefono(),
                cliente.getFechaCreacion(),
                cliente.getCuentas() != null ? cliente.getCuentas().size() : 0
        );
    }

    /**
     * Convierte un ClienteDTO recibido de una petición HTTP a una entidad Cliente.
     */
    public static Cliente toEntity(ClienteDTO dto) {
        if (dto == null) return null;

        return new Cliente(
                dto.getNombre(),
                dto.getApellidos(),
                dto.getDni(),
                dto.getEmail(),
                dto.getTelefono()
        );
    }
}
