package service;

import org.alopsalv.novabank.model.Cliente;
import org.alopsalv.novabank.model.Cuenta;
import org.alopsalv.novabank.repository.CuentaRepository;
import org.alopsalv.novabank.service.ClienteService;
import org.alopsalv.novabank.service.CuentaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CuentaServiceTest {

    @Mock
    private CuentaRepository cuentaRepository;

    @Mock
    private ClienteService clienteService;

    @InjectMocks
    private CuentaService cuentaService;

    // Variables clave para guardar los ID
    private Long idCliente1 = 1000L;
    private Long idCliente2 = 1001L;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("Debería de crear una cuenta correctamente con saldo inicial a 0")
    void crearCuentaBien() {
        when(clienteService.buscarCliente(idCliente1)).thenReturn(new Cliente());

        Cuenta nuevaCuenta = new Cuenta();
        nuevaCuenta.setClienteId(idCliente1);
        nuevaCuenta.setSaldo(BigDecimal.ZERO);
        nuevaCuenta.setFechaCreacion(LocalDateTime.now());

        Cuenta cuentaGuardadaMock = new Cuenta();
        cuentaGuardadaMock.setId(1000L);
        cuentaGuardadaMock.setClienteId(idCliente1);
        cuentaGuardadaMock.setSaldo(BigDecimal.ZERO);

        when(cuentaRepository.guardar(any(Cuenta.class))).thenReturn(cuentaGuardadaMock);

        Cuenta guardada = cuentaService.crearCuenta(nuevaCuenta);

        assertNotNull(guardada.getId());
        assertEquals(BigDecimal.ZERO, guardada.getSaldo());
    }

    @Test
    @DisplayName("Debería de lanzar error si intentamos crear una cuenta con saldo negativo")
    void crearCuentaSaldoNegativo() {
        Cuenta cuentaMala = new Cuenta();
        cuentaMala.setClienteId(idCliente1);
        cuentaMala.setSaldo(new BigDecimal("-100.00"));
        cuentaMala.setFechaCreacion(LocalDateTime.now());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            cuentaService.crearCuenta(cuentaMala);
        });

        assertTrue(exception.getMessage().toLowerCase().contains("saldo") || exception.getMessage().toLowerCase().contains("negativo"));
        verifyNoInteractions(cuentaRepository);
    }

    @Test
    @DisplayName("Debería de encontrar la cuenta por su IBAN")
    void buscarCuentaPorIban() {
        Cuenta cuentaMock = new Cuenta();
        cuentaMock.setClienteId(idCliente1);
        cuentaMock.setSaldo(BigDecimal.ZERO);
        cuentaMock.setNumeroCuenta("ES91210000000000000001");

        //Simulamos la base de datos devolviendo el Optional
        when(cuentaRepository.buscarPorNumero("ES91210000000000000001")).thenReturn(Optional.of(cuentaMock));

        Cuenta encontrada = cuentaService.buscarCuenta("ES91210000000000000001");

        assertNotNull(encontrada);
        assertEquals("ES91210000000000000001", encontrada.getNumeroCuenta());
    }

    @Test
    @DisplayName("Debería devolver la lista de cuentas de un cliente")
    void obtenerCuentasDeCliente() {
        Cuenta c1 = new Cuenta(); c1.setClienteId(idCliente1); c1.setSaldo(BigDecimal.ZERO);
        Cuenta c2 = new Cuenta(); c2.setClienteId(idCliente1); c2.setSaldo(BigDecimal.ZERO);

        //Configuramos el mock para que devuelva una lista con 2 cuentas
        when(cuentaRepository.buscarPorClienteId(idCliente1)).thenReturn(Arrays.asList(c1, c2));

        List<Cuenta> cuentasCliente1 = cuentaService.obtenerCuentasDeCliente(idCliente1);

        assertEquals(2, cuentasCliente1.size());
    }
}