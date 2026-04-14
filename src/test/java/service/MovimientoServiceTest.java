package service;

import org.alopsalv.novabank.model.Cuenta;
import org.alopsalv.novabank.model.Movimiento;
import org.alopsalv.novabank.model.TipoMovimiento;
import org.alopsalv.novabank.repository.CuentaRepository;
import org.alopsalv.novabank.repository.MovimientoRepository;
import org.alopsalv.novabank.service.MovimientoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovimientoServiceTest {

    @Mock
    private MovimientoRepository movimientoRepository;

    @Mock
    private CuentaRepository cuentaRepository;

    @InjectMocks
    private MovimientoService movimientoService;

    private Cuenta cuentaPrueba;

    @BeforeEach
    void setUp() {
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
        deposito.setTipo(TipoMovimiento.DEPOSITO);
        deposito.setCantidad(new BigDecimal("500.00"));
        deposito.setFecha(LocalDateTime.now());

        movimientoService.registrarMovimiento(deposito, cuentaPrueba);

        //El saldo esperado es 1500
        assertEquals(new BigDecimal("1500.00"), cuentaPrueba.getSaldo());

        //Comprobamos que el servicio le pidió al repositorio actualizar la BD
        verify(cuentaRepository).actualizarSaldo(1L, new BigDecimal("1500.00"));
        verify(movimientoRepository).guardar(deposito);
    }

    @Test
    @DisplayName("Debería restar el saldo correctamente al hacer un RETIRO")
    void registrarRetiroBien() {
        Movimiento retiro = new Movimiento();
        retiro.setCuentaId(1L);
        retiro.setTipo(TipoMovimiento.RETIRO);
        retiro.setCantidad(new BigDecimal("200.00"));
        retiro.setFecha(LocalDateTime.now());

        movimientoService.registrarMovimiento(retiro, cuentaPrueba);

        //El saldo era 1000, restamos 200, tiene que quedar en 800
        assertEquals(new BigDecimal("800.00"), cuentaPrueba.getSaldo());

        //Comprobamos que el servicio le pidió al repositorio actualizar la BD
        verify(cuentaRepository).actualizarSaldo(1L, new BigDecimal("800.00"));
        verify(movimientoRepository).guardar(retiro);
    }

    @Test
    @DisplayName("Debería lanzar error si se intenta mover una cantidad de 0 o negativa")
    void registrarMovimientoCantidadInvalida() {
        Movimiento movimientoMalo = new Movimiento();
        movimientoMalo.setCuentaId(1L);
        movimientoMalo.setTipo(TipoMovimiento.DEPOSITO);
        //Introducimos una cantidad negativa
        movimientoMalo.setCantidad(new BigDecimal("-50.00"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            movimientoService.registrarMovimiento(movimientoMalo, cuentaPrueba);
        });

        //Mensaje de error que se espera
        assertTrue(exception.getMessage().contains("mayor que cero"));

        //Como dio error, comprobamos que NO se tocó la BD para nada
        verifyNoInteractions(cuentaRepository);
        verifyNoInteractions(movimientoRepository);
    }

    @Test
    @DisplayName("Debería lanzar error si el RETIRO deja la cuenta en negativo")
    void registrarRetiroSaldoInsuficiente() {
        Movimiento retiroMasivo = new Movimiento();
        retiroMasivo.setCuentaId(1L);
        retiroMasivo.setTipo(TipoMovimiento.RETIRO);
        //Intentamos sacar 5000€ de una cuenta que solo tiene 1000€
        retiroMasivo.setCantidad(new BigDecimal("5000.00"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            movimientoService.registrarMovimiento(retiroMasivo, cuentaPrueba);
        });

        //Mensaje de error esperado
        assertTrue(exception.getMessage().contains("Saldo insuficiente"));

        //Comprobamos que el saldo no se ha modificado al saltar el error y sigue siendo 1000
        assertEquals(new BigDecimal("1000.00"), cuentaPrueba.getSaldo());

        //Como dio error, comprobamos que NO se guardó nada en la BD
        verifyNoInteractions(cuentaRepository);
        verifyNoInteractions(movimientoRepository);
    }

    @Test
    @DisplayName("Debería lanzar error al hacer TRANSFERENCIA sin saldo suficiente")
    void transferir_conSaldoInsuficiente_debeLanzarExcepcion() {
        //Configuramos el Mock para que cuando el servicio busque la cuenta origen, devuelva nuestra cuentaPrueba (1000€)
        when(cuentaRepository.buscarPorNumero(eq("ES91210000000000000001"), any()))
                .thenReturn(Optional.of(cuentaPrueba));

        //Configuramos el Mock para la cuenta de destino
        when(cuentaRepository.buscarPorNumero(eq("ES00000000000000000002"), any()))
                .thenReturn(Optional.of(new Cuenta()));

        //Intentamos transferir 5000€
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                movimientoService.transferir("ES91210000000000000001", "ES00000000000000000002", new BigDecimal("5000.00"))
        );

        assertTrue(exception.getMessage().contains("Saldo insuficiente"));

        //Verificamos que la transacción se cortó y NO se guardó ningún cambio de saldo ni movimiento
        verify(cuentaRepository, never()).actualizarSaldo(anyLong(), any(), any());
        verify(movimientoRepository, never()).guardar(any(), any());
    }
}