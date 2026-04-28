package service;

import org.alopsalv.novabank.dto.ClienteDTO;
import org.alopsalv.novabank.exception.ClienteNotFoundException;
import org.alopsalv.novabank.model.Cliente;
import org.alopsalv.novabank.repository.ClienteRepository;
import org.alopsalv.novabank.service.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    private Cliente clientePrueba;

    @BeforeEach
    void setUp() {
        clientePrueba = new Cliente();
        clientePrueba.setId(1L);
        clientePrueba.setNombre("Ana");
        clientePrueba.setApellidos("García");
        clientePrueba.setDni("12345678Z");
        clientePrueba.setEmail("ana@email.com");
    }

    @Test
    void obtenerCliente_Existente_DeberiaDevolverDTO() {

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clientePrueba));

        ClienteDTO resultado = clienteService.obtenerCliente(1L);

        assertNotNull(resultado);
        assertEquals("Ana", resultado.getNombre());
        assertEquals("12345678Z", resultado.getDni());
        verify(clienteRepository, times(1)).findById(1L);
    }

    @Test
    void obtenerCliente_NoExistente_DeberiaLanzarExcepcion() {

        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ClienteNotFoundException.class, () -> {
            clienteService.obtenerCliente(99L);
        });
        verify(clienteRepository, times(1)).findById(99L);
    }
}
