package org.alopsalv.novabank.repository;

import org.alopsalv.novabank.model.Cuenta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CuentaRepository {
    //Mapa para almacenar objeto cuenta.
    private final Map<String, Cuenta> cuentas = new HashMap<>();
    //Contador para el autoincrement.
    private long contadorCuentas = 1L;

    //Método para guardar una Cuenta.
    public Cuenta guardarCuenta(Cuenta cuenta){
        //Asigno id actual y luego incremento el contador.
        cuenta.setId(contadorCuentas++);
        //Agregamos al HashMap.
        cuentas.put(cuenta.getNumeroCuenta(), cuenta);
        return cuenta;
    }

    //Método para buscar una cuenta por numero de cuenta.
    public Cuenta buscarPorNumero(String numeroCuenta){
        return cuentas.get(numeroCuenta);
    }

    //Método para buscar un cliente por ID.
    public List<Cuenta> buscarPorClienteId(Long clienteId){
        List<Cuenta> cuentasDelCliente = new ArrayList<>();
        for (Cuenta cuenta : cuentas.values()){
            if (cuenta.getClienteId().equals(clienteId)){
                cuentasDelCliente.add(cuenta);
            }
        }
        return cuentasDelCliente;
    }

    //Método para almacenar en una lista las cuentas registradas.
    public List<Cuenta> obtenerTodos(){
        return new ArrayList<>(cuentas.values());
    }

}
