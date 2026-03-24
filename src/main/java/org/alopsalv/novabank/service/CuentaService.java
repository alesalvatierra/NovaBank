package org.alopsalv.novabank.service;

import org.alopsalv.novabank.model.Cuenta;
import org.alopsalv.novabank.repository.CuentaRepository;

import java.util.List;

public class CuentaService {

    private final CuentaRepository cuentaRepository;
    private final ClienteService clienteService;

    public CuentaService(CuentaRepository cuentaRepository, ClienteService clienteService) {
        this.cuentaRepository = cuentaRepository;
        this.clienteService = clienteService;
    }
    //Método para crear una cuenta validando que esta existe.
    public Cuenta crearCuenta(Cuenta cuentaNueva){

        var clienteExistente = clienteService.buscarCliente(cuentaNueva.getClienteId());
        if (clienteExistente == null){
            throw new IllegalArgumentException("ERROR: No se encontró ningún cliente con dni");
        }

        return cuentaRepository.guardarCuenta(cuentaNueva);
    }
    //Método para buscarCuenta por IBAN.
    public Cuenta buscarCuenta(String numeroCuenta){
        return cuentaRepository.buscarPorNumero(numeroCuenta);
    }

    //Método para obtener todas las cuentas del mismo cliente.
    public List<Cuenta> obtenerCuentasDeCliente(Long clienteId){
        return cuentaRepository.buscarPorClienteId(clienteId);
    }


}
