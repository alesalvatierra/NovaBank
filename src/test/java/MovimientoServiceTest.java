import org.alopsalv.novabank.model.Cuenta;
import org.alopsalv.novabank.model.Movimiento;
import org.alopsalv.novabank.repository.MovimientoRepository;
import org.alopsalv.novabank.service.MovimientoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class MovimientoServiceTest {

    private MovimientoService movimientoService;
    private MovimientoRepository movimientoRepository;
    private Cuenta cuentaPrueba;

    @BeforeEach
    void setUp() {
        movimientoRepository = new MovimientoRepository();
        movimientoService = new MovimientoService(movimientoRepository);

        //Preparamos una cuenta con 1000€ de saldo inicial para tests
        cuentaPrueba = new Cuenta();
        cuentaPrueba.setId(1L);
        cuentaPrueba.setNumeroCuenta("ES91210000000000000001");
        cuentaPrueba.setSaldo(new BigDecimal("1000.00"));
    }

    @Test
    @DisplayName("Debería de sumar el saldo correctamente al hacer un DEPOSITO")
    void registrarDepositoBien() {
        Movimiento deposito = new Movimiento();
        deposito.setCuentaId(1L);
        deposito.setTipo("DEPOSITO");
        deposito.setCantidad(new BigDecimal("500.00"));
        deposito.setFecha(LocalDateTime.now());

        movimientoService.registrarMovimiento(deposito, cuentaPrueba);

        //El saldo esperado es 1500
        assertEquals(new BigDecimal("1500.00"), cuentaPrueba.getSaldo());
    }

    @Test
    @DisplayName("Debería restar el saldo correctamente al hacer un RETIRO")
    void registrarRetiroBien() {
        Movimiento retiro = new Movimiento();
        retiro.setCuentaId(1L);
        retiro.setTipo("RETIRO");
        retiro.setCantidad(new BigDecimal("200.00"));
        retiro.setFecha(LocalDateTime.now());

        movimientoService.registrarMovimiento(retiro, cuentaPrueba);

        // El saldo era 1000, restamos 200, tiene que quedar en 800
        assertEquals(new BigDecimal("800.00"), cuentaPrueba.getSaldo());
    }

    @Test
    @DisplayName("Debería lanzar error si se intenta mover una cantidad de 0 o negativa")
    void registrarMovimientoCantidadInvalida() {
        Movimiento movimientoMalo = new Movimiento();
        movimientoMalo.setCuentaId(1L);
        movimientoMalo.setTipo("DEPOSITO");
        //Introducimos una cantidad negativa
        movimientoMalo.setCantidad(new BigDecimal("-50.00"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            movimientoService.registrarMovimiento(movimientoMalo, cuentaPrueba);
        });
        //Mensaje de error que se espera
        assertTrue(exception.getMessage().contains("mayor que cero"));
    }

    @Test
    @DisplayName("Debería lanzar error si el RETIRO deja la cuenta en negativo")
    void registrarRetiroSaldoInsuficiente() {
        Movimiento retiroMasivo = new Movimiento();
        retiroMasivo.setCuentaId(1L);
        retiroMasivo.setTipo("RETIRO");
        //Intentamos sacar 5000€ de una cuenta que solo tiene 1000€
        retiroMasivo.setCantidad(new BigDecimal("5000.00"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            movimientoService.registrarMovimiento(retiroMasivo, cuentaPrueba);
        });
        //Mensaje de error esperado
        assertTrue(exception.getMessage().contains("Saldo insuficiente"));

        //Comprobamos que el saldo no se ha modificado al saltar el error y sigue siendo 1000
        assertEquals(new BigDecimal("1000.00"), cuentaPrueba.getSaldo());
    }
}