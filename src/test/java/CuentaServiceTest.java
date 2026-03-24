import org.alopsalv.novabank.model.Cliente;
import org.alopsalv.novabank.model.Cuenta;
import org.alopsalv.novabank.repository.ClienteRepository;
import org.alopsalv.novabank.repository.CuentaRepository;
import org.alopsalv.novabank.service.ClienteService;
import org.alopsalv.novabank.service.CuentaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CuentaServiceTest {

    private CuentaService cuentaService;
    private CuentaRepository cuentaRepository;
    private ClienteService clienteService;
    private ClienteRepository clienteRepository;

    //Variables clave para guardar los ID
    private Long idCliente1;
    private Long idCliente2;

    @BeforeEach
    void setUp() {
        clienteRepository = new ClienteRepository();
        clienteService = new ClienteService(clienteRepository);
        cuentaRepository = new CuentaRepository();
        cuentaService = new CuentaService(cuentaRepository, clienteService);

        //Creamos al cliente 1 y guardamos su ID
        Cliente c1 = new Cliente("Alejandro", "López", "12345678A", "alejandro@nttdata.com", "612345678");
        Cliente guardado1 = clienteService.crearCliente(c1);
        idCliente1 = guardado1.getId();

        //Creamos al cliente 2 y guardamos su ID
        Cliente c2 = new Cliente("Maria", "Clemente", "23456789B", "maria@nttdata.com", "623456789");
        Cliente guardado2 = clienteService.crearCliente(c2);
        idCliente2 = guardado2.getId();
    }

    @Test
    @DisplayName("Debería de crear una cuenta correctamente con saldo inicial a 0")
    void crearCuentaBien() {
        Cuenta nuevaCuenta = new Cuenta();
        nuevaCuenta.setClienteId(idCliente1);
        nuevaCuenta.setSaldo(BigDecimal.ZERO);
        nuevaCuenta.setFechaCreacion(LocalDateTime.now());

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
    }

    @Test
    @DisplayName("Debería de encontrar la cuenta por su IBAN")
    void buscarCuentaPorIban() {
        Cuenta cuenta = new Cuenta();
        cuenta.setClienteId(idCliente1);
        cuenta.setSaldo(BigDecimal.ZERO);
        cuenta.setNumeroCuenta("ES91210000000000000001");
        cuentaService.crearCuenta(cuenta);

        Cuenta encontrada = cuentaService.buscarCuenta("ES91210000000000000001");

        assertNotNull(encontrada);
        assertEquals("ES91210000000000000001", encontrada.getNumeroCuenta());
    }

    @Test
    @DisplayName("Debería devolver la lista de cuentas de un cliente")
    void obtenerCuentasDeCliente() {
        Cuenta c1 = new Cuenta(); c1.setClienteId(idCliente1); c1.setSaldo(BigDecimal.ZERO);
        Cuenta c2 = new Cuenta(); c2.setClienteId(idCliente1); c2.setSaldo(BigDecimal.ZERO);
        cuentaService.crearCuenta(c1);
        cuentaService.crearCuenta(c2);

        Cuenta c3 = new Cuenta(); c3.setClienteId(idCliente2); c3.setSaldo(BigDecimal.ZERO);
        cuentaService.crearCuenta(c3);

        List<Cuenta> cuentasCliente1 = cuentaService.obtenerCuentasDeCliente(idCliente1);

        assertEquals(2, cuentasCliente1.size());
    }
}