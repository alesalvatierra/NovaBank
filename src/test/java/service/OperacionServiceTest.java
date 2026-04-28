package service;

import org.alopsalv.novabank.dto.CuentaDTO;
import org.alopsalv.novabank.dto.OperacionDTO;
import org.alopsalv.novabank.exception.SaldoInsuficienteException;
import org.alopsalv.novabank.model.Cuenta;
import org.alopsalv.novabank.model.TipoMovimiento;
import org.alopsalv.novabank.repository.CuentaRepository;
import org.alopsalv.novabank.repository.MovimientoRepository;
import org.alopsalv.novabank.service.OperacionService; // Importamos tu servicio original
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OperacionServiceTest {

    @Mock
    private CuentaRepository cuentaRepository;

    @Mock
    private MovimientoRepository movimientoRepository;

    @InjectMocks
    private OperacionService operacionService;

    private Cuenta cuentaPrueba;

    @BeforeEach
    void setUp() {
        cuentaPrueba = new Cuenta();
        cuentaPrueba.setId(1L);
        cuentaPrueba.setNumeroCuenta("ES123456789");
        cuentaPrueba.setSaldo(new BigDecimal("100.00"));
    }

    @Test
    void realizarDeposito_DeberiaAumentarSaldo() {

        OperacionDTO opDTO = new OperacionDTO("ES123456789", new BigDecimal("50.00"), "Ingreso nómina");

        when(cuentaRepository.findByNumeroCuenta("ES123456789")).thenReturn(Optional.of(cuentaPrueba));
        when(cuentaRepository.save(any(Cuenta.class))).thenReturn(cuentaPrueba);

        CuentaDTO resultado = operacionService.realizarOperacion(opDTO, TipoMovimiento.DEPOSITO);

        assertNotNull(resultado);
        assertEquals(new BigDecimal("150.00"), cuentaPrueba.getSaldo(), "El saldo debería ser 150.00");

        verify(movimientoRepository, times(1)).save(any());
        verify(cuentaRepository, times(1)).save(any());
    }

    @Test
    void realizarRetiro_SinSaldoSuficiente_DeberiaLanzarExcepcion() {

        OperacionDTO opDTO = new OperacionDTO("ES123456789", new BigDecimal("200.00"), "Compra online");
        when(cuentaRepository.findByNumeroCuenta("ES123456789")).thenReturn(Optional.of(cuentaPrueba));

        assertThrows(SaldoInsuficienteException.class, () -> {
            operacionService.realizarOperacion(opDTO, TipoMovimiento.RETIRO);
        });

        verify(movimientoRepository, never()).save(any());
        verify(cuentaRepository, never()).save(any());
    }
}
