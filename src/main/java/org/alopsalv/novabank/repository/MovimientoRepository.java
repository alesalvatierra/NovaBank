package org.alopsalv.novabank.repository;

import org.alopsalv.novabank.model.Movimiento;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovimientoRepository {
    //Mapa para almacenar objeto movimiento.
    private final Map<Long, Movimiento> movimientos = new HashMap<>();
    //Contador para el autoincrement.
    private long contadorMovimientos = 1L;

    //Método para guardar un Movimiento.
    public Movimiento guardarMovimiento(Movimiento movimiento){
        //Asigno id actual y luego incremento el contador.
        movimiento.setId(contadorMovimientos++);
        //Agregamos al HashMap.
        movimientos.put(movimiento.getId(), movimiento);
        return movimiento;
    }

    //Método para buscar movimientos por ID de la cuenta asociada.
    public List<Movimiento> buscarPorCuentaId(Long cuentaId){
        List<Movimiento> movimientosDeLaCuenta = new ArrayList<>();
        for (Movimiento movimiento : movimientos.values()){
            // Comprobamos si el ID de la cuenta coincide.
            if (movimiento.getCuentaId().equals(cuentaId)){
                movimientosDeLaCuenta.add(movimiento);
            }
        }
        return movimientosDeLaCuenta;
    }

    //Método para almacenar en una lista los movimientos registrados.
    public List<Movimiento> obtenerTodos(){
        return new ArrayList<>(movimientos.values());
    }
}
