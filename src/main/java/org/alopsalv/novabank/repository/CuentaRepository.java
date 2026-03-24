package org.alopsalv.novabank.repository;

import org.alopsalv.novabank.model.Cuenta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CuentaRepository {

    private final Map<Long, Cuenta> cuentas = new HashMap<>();
    private long contadorCuentas = 1L;

    //Método para guardar una Cuenta
    public Cuenta guardarCuenta(Cuenta cuenta){
        //Si no tiene ID, le asignamos uno nuevo
        if (cuenta.getId() == null) {
            cuenta.setId(contadorCuentas++);
        }
        //Guardamos usando el ID para que nunca se sobreescriban
        cuentas.put(cuenta.getId(), cuenta);
        return cuenta;
    }

    // Método para buscar una cuenta por IBAN
    public Cuenta buscarPorNumero(String numeroCuenta){
        //Recorremos las cuentas buscando la que coincida
        for (Cuenta cuenta : cuentas.values()){
            if (cuenta.getNumeroCuenta() != null && cuenta.getNumeroCuenta().equals(numeroCuenta)){
                return cuenta;
            }
        }
        return null; //Null si no existe.
    }

    //Método para buscar cuentas por ID de cliente
    public List<Cuenta> buscarPorClienteId(Long clienteId){
        List<Cuenta> cuentasDelCliente = new ArrayList<>();
        for (Cuenta cuenta : cuentas.values()){
            if (cuenta.getClienteId().equals(clienteId)){
                cuentasDelCliente.add(cuenta);
            }
        }
        return cuentasDelCliente;
    }

    //Método para almacenar en una lista las cuentas registradas
    public List<Cuenta> obtenerTodos(){
        return new ArrayList<>(cuentas.values());
    }
}