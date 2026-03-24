package org.alopsalv.novabank;

import org.alopsalv.novabank.model.Cliente;
import org.alopsalv.novabank.model.Cuenta;
import org.alopsalv.novabank.model.Movimiento;
import org.alopsalv.novabank.repository.ClienteRepository;
import org.alopsalv.novabank.repository.CuentaRepository;
import org.alopsalv.novabank.repository.MovimientoRepository;
import org.alopsalv.novabank.service.ClienteService;
import org.alopsalv.novabank.service.CuentaService;
import org.alopsalv.novabank.service.MovimientoService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        //Instanciamos los repositorios
        ClienteRepository clienteRepo = new ClienteRepository();
        CuentaRepository cuentaRepo = new CuentaRepository();
        MovimientoRepository movimientoRepo = new MovimientoRepository();

        //Instanciamos los servicios
        ClienteService clienteService = new ClienteService(clienteRepo);
        CuentaService cuentaService = new CuentaService(cuentaRepo, clienteService);
        MovimientoService movimientoService = new MovimientoService(movimientoRepo);

        Scanner scanner = new Scanner(System.in);
        int opcionPrincipal = -1;   //Establecemos la opción a -1

        //Menú que se va a repetir mientras se cumpla el while.
        do {
            System.out.println("====================================");
            System.out.println("NOVABANK - SISTEMA DE OPERACIONES");
            System.out.println("====================================");
            System.out.println("1. Gestión de clientes");
            System.out.println("2. Gestión de cuentas");
            System.out.println("3. Operaciones financieras");
            System.out.println("4. Consultas");
            System.out.println("5. Salir");
            System.out.println(" ");
            System.out.print("Seleccione una opción: ");
            opcionPrincipal = scanner.nextInt();
            scanner.nextLine();
            //Submenús
            switch (opcionPrincipal) {
                case 1:
                    int opcionCliente = -1;
                    do {
                        System.out.println("--- GESTIÓN DE CLIENTES ---");
                        System.out.println("1. Crear cliente");
                        System.out.println("2. Buscar cliente");
                        System.out.println("3. Listar clientes");
                        System.out.println("4. Volver");
                        opcionCliente = scanner.nextInt();
                        scanner.nextLine();

                        switch (opcionCliente){
                            case 1:
                                System.out.println("--- CREACIÓN DE CLIENTE ---");
                                System.out.print("Introduzca el nombre: ");
                                String nombre = scanner.nextLine();
                                System.out.print("Introduzca los apellidos: ");
                                String apellidos = scanner.nextLine();
                                System.out.print("Introduzca el DNI/NIF: ");
                                String dni = scanner.nextLine();
                                System.out.print("Introduzca el email: ");
                                String email = scanner.nextLine();
                                System.out.print("Introduzca el teléfono: ");
                                String telefono = scanner.nextLine();
                                Cliente nuevoCliente = new Cliente(nombre, apellidos, dni, email, telefono);

                                try {
                                    Cliente clienteGuardado = clienteService.crearCliente(nuevoCliente);
                                    System.out.println("Cliente creado correctamente.");
                                    System.out.println("ID cliente: " + clienteGuardado.getId());

                                }catch (IllegalArgumentException e){
                                    System.err.println("ERROR: " + e.getMessage());
                                }
                                break;
                            case 2:
                                System.out.println("--- BUSCAR CLIENTE ---");
                                System.out.println("1. Por ID interno");
                                System.out.println("2. Por DNI");
                                System.out.print("Elija criterio de búsqueda: ");
                                int opcionBuscar = scanner.nextInt();
                                scanner.nextLine();

                                Cliente clienteEncontrado = null;

                                if (opcionBuscar == 1){
                                    System.out.print("Introduzca el ID del cliente: ");
                                    try {
                                        Long idBusqueda = Long.parseLong(scanner.nextLine());
                                        clienteEncontrado = clienteService.buscarCliente(idBusqueda);
                                    } catch (NumberFormatException e){
                                        System.err.println("ERROR: El ID debe ser un valor numérico.");
                                    }
                                } else if (opcionBuscar == 2) {
                                    System.out.print("Introduzca el DNI del cliente: ");
                                    String dniBusqueda = scanner.nextLine();
                                    clienteEncontrado = clienteService.buscarClientePorDni(dniBusqueda);
                                } else {
                                        System.out.println("Opción no válida");
                                }
                                if (clienteEncontrado != null) {
                                    System.out.println("Cliente encontrado: ");
                                    System.out.println("ID: " + clienteEncontrado.getId());
                                    System.out.println("Nombre: " + clienteEncontrado.getNombre());
                                    System.out.println("DNI: " + clienteEncontrado.getDni());
                                    System.out.println("Email: " + clienteEncontrado.getEmail());
                                    System.out.println("Teléfono: " + clienteEncontrado.getTelefono());
                                } else if (opcionBuscar == 1 || opcionBuscar == 2) {
                                    System.out.println("No se encontró ningún cliente con esos datos.");
                                }
                                break;
                            case 3:
                                System.out.println("--- LISTADO DE CLIENTES ---");
                                //Obtenemos la lista de Clientes.
                                var listaClientes = clienteService.obtenerTodosLosClientes();

                                //Comprobamos si la lista está vacía.
                                if (listaClientes == null || listaClientes.isEmpty()) {
                                    System.out.println("No hay clientes registrados en el sistema.");
                                } else {
                                    //Imprimimos la cabecera de la tabla
                                    System.out.printf("%-5s | %-35s | %-10s | %-35s | %-10s%n", "ID", "Nombre", "DNI", "Email", "Teléfono");
                                    System.out.println("------|-------------------------------------|------------|-------------------------------------|-----------");
                                    //Recorremos la lista y escribimos cada fila
                                    for (Cliente c : listaClientes) {
                                        //Juntamos el nombre y los apellidos para que quede en una sola columna
                                        String nombreCompleto = c.getNombre() + " " + c.getApellidos();

                                        //Escribimos los datos en cada espacio correspondiente
                                        System.out.printf("%-5d | %-35s | %-10s | %-35s | %-10s%n",
                                                c.getId(),
                                                nombreCompleto,
                                                c.getDni(),
                                                c.getEmail(),
                                                c.getTelefono());
                                    }
                                }
                                break;
                        }
                    } while (opcionCliente !=4);
                    break;

                case 2:
                    int opcionCuenta = -1;
                    do {
                        System.out.println("--- GESTIÓN DE CUENTAS ---");
                        System.out.println("1. Crear cuenta");
                        System.out.println("2. Listar cuentas de cliente");
                        System.out.println("3. Ver información de cuenta");
                        System.out.println("4. Volver");
                        opcionCuenta = scanner.nextInt();
                        scanner.nextLine();

                        switch (opcionCuenta){
                            case 1:
                                System.out.println("--- CREAR CUENTA ---");
                                System.out.print("Introduzca el ID del cliente titular: ");

                                Long idClienteTitular;
                                try {
                                    //Parseamos el Long para controlar excepciones
                                    idClienteTitular = Long.parseLong(scanner.nextLine());
                                } catch (NumberFormatException e) {
                                    System.err.println("ERROR: El ID debe ser un valor numérico válido.");
                                    break;
                                }

                                // Comprobamos que el cliente existe
                                Cliente titular = clienteService.buscarCliente(idClienteTitular);

                                if (titular == null) {
                                    // Si no existe, cancelamos la operación
                                    System.err.println("ERROR: No existe ningún cliente registrado con el ID " + idClienteTitular);
                                } else {
                                    // Si existe, seguimos aquí dentro
                                    Cuenta nuevaCuenta = new Cuenta();
                                    nuevaCuenta.setClienteId(idClienteTitular);
                                    // Saldo inicial a 0€
                                    nuevaCuenta.setSaldo(java.math.BigDecimal.ZERO);
                                    // Guardamos fecha y hora actual
                                    nuevaCuenta.setFechaCreacion(java.time.LocalDateTime.now());

                                    try {
                                        // Validamos a través del servicio y guardamos la cuenta
                                        Cuenta cuentaGuardada = cuentaService.crearCuenta(nuevaCuenta);

                                        // Generamos el IBAN usando el ID de la cuenta rellenando con ceros a la izquierda
                                        String ibanGenerado = String.format("ES91210000%012d", cuentaGuardada.getId());
                                        cuentaGuardada.setNumeroCuenta(ibanGenerado);

                                        // Imprimimos los datos
                                        System.out.println("Cuenta creada correctamente.");
                                        System.out.println("Número de cuenta: " + ibanGenerado);
                                        System.out.println("Titular: " + titular.getNombre() + " " + titular.getApellidos() + " (ID: " + titular.getId() + ")");
                                        System.out.println("Saldo inicial: 0,00 €");

                                    } catch (IllegalArgumentException e) {
                                        System.err.println("ERROR: " + e.getMessage());
                                    }
                                }
                                break;

                            case 2:
                                System.out.println("--- LISTAR CUENTAS DEL CLIENTE ---");
                                System.out.print("Introduzca ID del cliente: ");
                                Long idBusquedaCuentas;
                                try {
                                    idBusquedaCuentas = Long.parseLong(scanner.nextLine());
                                } catch (NumberFormatException e) {
                                    System.err.println("ERROR: El ID debe ser un valor numérico válido.");
                                    break;
                                }
                                //Buscamos al cliente para comprobar si existe
                                Cliente clienteDueno = clienteService.buscarCliente(idBusquedaCuentas);

                                if (clienteDueno == null) {
                                    System.err.println("ERROR: No existe ningún cliente registrado con el ID " + idBusquedaCuentas);
                                } else {
                                    //Usamos el método del servicio para sacar la lista de cuentas del cliente
                                    List<Cuenta> cuentasDelCliente = cuentaService.obtenerCuentasDeCliente(idBusquedaCuentas);
                                    System.out.println("Cuentas del cliente " + clienteDueno.getNombre() + " " + clienteDueno.getApellidos() + ":");
                                    if (cuentasDelCliente.isEmpty()) {
                                        System.out.println("Este cliente aún no tiene cuentas bancarias abiertas.");
                                    } else {
                                        System.out.printf("%-25s | %-15s%n", "Número de cuenta", "Saldo");
                                        System.out.println("--------------------------|----------------");

                                        //Recorremos la lista y escribimos cada fila
                                        for (Cuenta c : cuentasDelCliente) {
                                            System.out.printf("%-25s | %s €%n", c.getNumeroCuenta(), c.getSaldo());
                                        }
                                    }
                                }
                                break;
                            case 3:
                                System.out.println("--- INFORMACIÓN DE CUENTA ---");
                                System.out.print("Introduzca número de cuenta: ");
                                String numeroCuentaBuscada = scanner.nextLine();

                                //Buscamos la cuenta por el IBAN a través del servicio
                                Cuenta cuentaEncontrada = cuentaService.buscarCuenta(numeroCuentaBuscada);

                                if (cuentaEncontrada == null){
                                    System.err.println("ERROR: No se encontró ninguna cuenta con el número " + numeroCuentaBuscada);
                                } else {
                                    //Buscamos el titular usando el ID.
                                    Cliente titularCuenta = clienteService.buscarCliente(cuentaEncontrada.getClienteId());
                                    //Formateamos la fecha
                                    java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                                    String fechaFormateada = cuentaEncontrada.getFechaCreacion().format(formatter);

                                    System.out.println("Número de cuenta: " + cuentaEncontrada.getNumeroCuenta());
                                    if (titularCuenta!= null){
                                        System.out.println("Titular: " + titularCuenta.getNombre() + " " + titularCuenta.getApellidos());
                                    } else {
                                        System.out.println("Titular: Cliente Desconocido");
                                    }
                                }

                                break;
                        }

                    } while (opcionCuenta !=4);
                    break;

                case 3:
                    int opcionOperaciones = -1;
                    do {
                        System.out.println("--- OPERACIONES FINANCIERAS ---");
                        System.out.println("1. Depositar dinero");
                        System.out.println("2. Retirar dinero");
                        System.out.println("3. Transferencia entre cuentas");
                        System.out.println("4. Volver");
                        opcionOperaciones = scanner.nextInt();
                        scanner.nextLine();

                        switch (opcionOperaciones){
                            case 1:
                                System.out.println("--- DEPOSITAR DINERO ---");
                                System.out.print("Introduzca el número de cuenta (IBAN): ");
                                String numCuentaDeposito = scanner.nextLine();

                                //Validamos que exista la cuenta
                                Cuenta cuentaDeposito = cuentaService.buscarCuenta(numCuentaDeposito);

                                if (cuentaDeposito == null){
                                    System.err.println("ERROR: No se ha encontrado la cuenta con el IBAN: " + numCuentaDeposito);
                                } else {
                                    System.out.print("Introduzca la cantidad a depositar: ");
                                    BigDecimal cantidadDeposito;

                                    try{
                                        //Convertimos entrada a BigDecimal
                                        cantidadDeposito = new BigDecimal(scanner.nextLine());
                                    } catch (NumberFormatException e){
                                        System.err.println("ERROR: La cantidad debe ser un valor numérico");
                                        break;
                                    }

                                    if (cantidadDeposito.compareTo(BigDecimal.ZERO) <= 0){
                                        System.err.println("ERROR: La cantidad a depositar debe ser mayor que 0");
                                    }else {
                                        Movimiento deposito = new Movimiento();
                                        //Obtenemos el ID
                                        deposito.setCuentaId(cuentaDeposito.getId());
                                        //Seleccionamos tipo "DEPOSITO"
                                        deposito.setTipo("DEPOSITO");
                                        //Llamamos a la cantidad seleccionada
                                        deposito.setCantidad(cantidadDeposito);
                                        //Guardamos la fecha actual
                                        deposito.setFecha(LocalDateTime.now());

                                        //Llamamos al servicio para que registre el movimiento
                                        movimientoService.registrarMovimiento(deposito,cuentaDeposito);
                                        //Imprimimos resultados
                                        System.out.println("Depósito realizado correctamente.");
                                        System.out.println("Cuenta: " + cuentaDeposito.getNumeroCuenta());
                                        System.out.printf("Importe: +%,.2f €%n", cantidadDeposito);
                                        System.out.printf("Nuevo saldo: %,.2f €%n", cuentaDeposito.getSaldo());
                                    }
                                }
                                break;

                            case 2:
                                System.out.println("--- RETIRAR DINERO ---");
                                System.out.print("Introduzca el número de cuenta (IBAN): ");
                                String numCuentaRetirada = scanner.nextLine();

                                //Validamos que exista la cuenta
                                Cuenta cuentaRetirada = cuentaService.buscarCuenta(numCuentaRetirada);

                                if (cuentaRetirada == null){
                                    System.err.println("ERROR: No se ha encontrado la cuenta con el IBAN: " + numCuentaRetirada);
                                } else {
                                    System.out.print("Introduzca la cantidad a retirar: ");
                                    BigDecimal cantidadRetirada;

                                    try{
                                        //Convertimos entrada a BigDecimal
                                        cantidadRetirada = new BigDecimal(scanner.nextLine());
                                    } catch (NumberFormatException e){
                                        System.err.println("ERROR: La cantidad debe ser un valor numérico");
                                        break;
                                    }

                                    if (cantidadRetirada.compareTo(BigDecimal.ZERO) <= 0) {
                                        System.err.println("ERROR: La cantidad a retirar debe ser mayor que 0");
                                    } else if(cuentaRetirada.getSaldo().compareTo(cantidadRetirada) < 0) {
                                        System.err.println("ERROR: Saldo insuficiente. Su saldo actual es de " +cuentaRetirada.getSaldo() + "€");
                                    }else {
                                        Movimiento retirada = new Movimiento();
                                        //Obtenemos el ID
                                        retirada.setCuentaId(cuentaRetirada.getId());
                                        //Seleccionamos tipo "RETIRADA"
                                        retirada.setTipo("RETIRADA");
                                        //Llamamos a la cantidad seleccionada
                                        retirada.setCantidad(cantidadRetirada);
                                        //Guardamos la fecha actual
                                        retirada.setFecha(LocalDateTime.now());

                                        //Llamamos al servicio para que registre el movimiento
                                        movimientoService.registrarMovimiento(retirada,cuentaRetirada);
                                        //Imprimimos resultados
                                        System.out.println("Retirada realizada correctamente.");
                                        System.out.println("Cuenta: " + cuentaRetirada.getNumeroCuenta());
                                        System.out.printf("Importe: -%,.2f €%n", cantidadRetirada); // Corregido el símbolo a negativo
                                        System.out.printf("Nuevo saldo: %,.2f €%n", cuentaRetirada.getSaldo());
                                    }
                                }
                                break;
                            case 3:
                                System.out.println("--- TRANSFERENCIAS ENTRE CUENTAS ---");
                                //Solicitamos el IBAN de origen
                                System.out.print("Introduzca el número de cuenta de ORIGEN (IBAN): ");
                                String numCuentaOrigen = scanner.nextLine();
                                Cuenta cuentaOrigen = cuentaService.buscarCuenta(numCuentaOrigen);
                                //Validamos que la cuenta Origen no sea null
                                if (cuentaOrigen == null) {
                                    System.err.println("ERROR: No se encontró la cuenta de origen.");
                                    break;
                                }
                                System.out.print("Introduzca el número de cuenta de DESTINO (IBAN): ");
                                String numCuentaDestino = scanner.nextLine();
                                Cuenta cuentaDestino = cuentaService.buscarCuenta(numCuentaDestino);
                                //Validamos que la cuenta Destino no sea null
                                if (cuentaDestino == null) {
                                    System.err.println("ERROR: No se encontró la cuenta de destino.");
                                    break;
                                }
                                //Validamos que no sean la misma cuenta
                                if (cuentaOrigen.getId().equals(cuentaDestino.getId())) {
                                    System.err.println("ERROR: No se puede realizar una transferencia a la misma cuenta.");
                                    break;
                                }
                                //Solicitamos el importe a transferir
                                System.out.print("Introduzca la cantidad a transferir: ");
                                BigDecimal cantidadTransferencia;

                                try {
                                    cantidadTransferencia = new BigDecimal(scanner.nextLine());
                                }catch (NumberFormatException e){
                                    System.err.println("ERROR: La cantidad debe ser un valor numérico válido.");
                                    break;
                                }

                                if (cantidadTransferencia.compareTo(BigDecimal.ZERO) <= 0){
                                    System.err.println("ERROR: La cantidad debe ser mayor que 0.");
                                }else if (cuentaOrigen.getSaldo().compareTo(cantidadTransferencia) < 0) {
                                    System.err.println("ERROR: Saldo insuficiente en la cuenta de origen. Saldo actual: " + cuentaOrigen.getSaldo() + " €");
                                } else {
                                    try {
                                        //Retiramos el importe de la cuenta origen y registramos como TRANSFERENCIA_SALIENTE
                                        Movimiento salida = new Movimiento();
                                        salida.setCuentaId(cuentaOrigen.getId());
                                        salida.setTipo("TRANSFERENCIA_SALIENTE");
                                        salida.setCantidad(cantidadTransferencia);
                                        salida.setFecha(LocalDateTime.now());
                                        movimientoService.registrarMovimiento(salida, cuentaOrigen);

                                        //Depositamos el importe de la cuenta de destino y registramos como TRANSFERENCIA_ENTRANTE
                                        Movimiento entrada = new Movimiento();
                                        entrada.setCuentaId(cuentaDestino.getId());
                                        entrada.setTipo("TRANSFERENCIA_ENTRANTE");
                                        entrada.setCantidad(cantidadTransferencia);
                                        entrada.setFecha(LocalDateTime.now());
                                        movimientoService.registrarMovimiento(entrada, cuentaDestino);

                                        //Imprimimos el ticket
                                        System.out.println("Transferencia realizada correctamente.");
                                        System.out.printf("Cuenta origen: %s → -%,.2f €%n", cuentaOrigen.getNumeroCuenta(), cantidadTransferencia);
                                        System.out.printf("Cuenta destino: %s → +%,.2f €%n", cuentaDestino.getNumeroCuenta(), cantidadTransferencia);

                                    } catch (Exception e) {
                                        System.err.println("ERROR crítico durante la transferencia: " + e.getMessage());
                                    }
                                }
                                break;
                        }
                    } while (opcionOperaciones !=4);
                    break;

                case 4:
                    int opcionConsultas = -1;
                    do {
                        System.out.println("--- CONSULTAS ---");
                        System.out.println("1. Consultar saldo");
                        System.out.println("2. Historial de movimientos");
                        System.out.println("3. Movimientos por rango de fechas");
                        System.out.println("4. Volver");
                        opcionConsultas = scanner.nextInt();
                        scanner.nextLine();

                        switch (opcionConsultas){
                            case 1:
                                System.out.println("--- CONSULTAR SALDO ---");
                                System.out.print("Introduzca el número de cuenta (IBAN): ");
                                String numCuentaSaldo = scanner.nextLine();

                                //Buscamos la cuenta usando el servicio
                                Cuenta cuentaSaldo = cuentaService.buscarCuenta(numCuentaSaldo);

                                if (cuentaSaldo == null) {
                                    System.err.println("ERROR: No se encontró ninguna cuenta con el número " + numCuentaSaldo);
                                } else {
                                    //Imprimimos el saldo
                                    System.out.println("Consulta de saldo exitosa.");
                                    System.out.println("Cuenta: " + cuentaSaldo.getNumeroCuenta());
                                    System.out.printf("Saldo disponible: %,.2f €%n", cuentaSaldo.getSaldo());
                                }
                                break;

                            case 2:
                                System.out.println("---HISTORIAL DE MOVIMIENTOS---");
                                System.out.println("Introduzca el número de cuenta (IBAN): ");
                                String numCuentaHistorial = scanner.nextLine();

                                Cuenta cuentaHistorial = cuentaService.buscarCuenta(numCuentaHistorial);

                                if (cuentaHistorial == null){
                                    System.err.println("ERROR: No se encontró ninguna cuenta con el número " + numCuentaHistorial);
                                } else {
                                    List<Movimiento> listaMovimientos = movimientoService.obtenerMovimientosDeCuenta(cuentaHistorial.getId());

                                    if (listaMovimientos.isEmpty()){
                                        System.out.println("Esta cuenta aún no tiene ningún movimiento registrado.");
                                    } else {
                                        //Imprimimos la cabecera de la tabla
                                        System.out.println("Historial de la cuenta: " + cuentaHistorial.getNumeroCuenta());
                                        System.out.printf("%-5s | %-20s | %-25s | %-15s%n", "ID", "Fecha", "Tipo", "Importe");
                                        System.out.println("------|----------------------|---------------------------|----------------");

                                        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                                        for (Movimiento m : listaMovimientos){
                                            String fechaLimpia = m.getFecha().format(fmt);

                                            //Calculamos si es Ingreso o Gasto
                                            String signo = (m.getTipo().equals("DEPOSITO") || m.getTipo().equals("TRANSFERENCIA_ENTRANTE")) ? "+" : "-";
                                            System.out.printf("%-5d | %-20s | %-25s | %s%,.2f €%n",
                                                    m.getId(),
                                                    fechaLimpia,
                                                    m.getTipo(),
                                                    signo,
                                                    m.getCantidad());
                                        }
                                    }
                                }
                                break;

                            case 3:

                                break;
                        }

                    } while (opcionConsultas !=4);
                    break;

            }

        }while (opcionPrincipal !=5);

        System.out.println("Gracias por usar NovaBank :)");
        scanner.close();
    }
}