package service;

import org.alopsalv.novabank.dto.CuentaDTO;
import org.alopsalv.novabank.exception.ClienteNotFoundException;
import org.alopsalv.novabank.model.Cliente;
import org.alopsalv.novabank.model.Cuenta;
import org.alopsalv.novabank.repository.ClienteRepository;
import org.alopsalv.novabank.repository.CuentaRepository;
import org.alopsalv.novabank.service.CuentaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong; // Importante para evitar errores de tipo
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CuentaServiceTest {

    @Mock
    private CuentaRepository cuentaRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private CuentaService cuentaService;

    private Cliente clientePrueba;
    private Cuenta cuentaPrueba;

    @BeforeEach
    void setUp() {
        // Aseguramos que los IDs sean Long (con la L al final)
        clientePrueba = new Cliente();
        clientePrueba.setId(1L);
        clientePrueba.setDni("12345678Z");

        cuentaPrueba = new Cuenta();
        cuentaPrueba.setId(1L);
        cuentaPrueba.setNumeroCuenta("ES123456");
        cuentaPrueba.setSaldo(BigDecimal.ZERO);
        cuentaPrueba.setCliente(clientePrueba);
    }

    @Test
    void crearCuenta_ClienteExistente_DeberiaGuardarCuenta() {
        // Arrange
        // Usamos anyLong() para que no importe si pasa un 1 (int) o un 1L (long)
        when(clienteRepository.findById(anyLong())).thenReturn(Optional.of(clientePrueba));
        when(cuentaRepository.save(any(Cuenta.class))).thenReturn(cuentaPrueba);

        // Act
        CuentaDTO resultado = cuentaService.crearCuenta(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals("ES123456", resultado.getNumeroCuenta());
        verify(cuentaRepository, times(1)).save(any(Cuenta.class));
    }

    @Test
    void crearCuenta_ClienteNoExistente_DeberiaLanzarExcepcion() {
        // Arrange
        when(clienteRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ClienteNotFoundException.class, () -> {
            cuentaService.crearCuenta(99L);
        });

        verify(cuentaRepository, never()).save(any(Cuenta.class));
    }
}
