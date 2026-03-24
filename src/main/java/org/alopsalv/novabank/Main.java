package org.alopsalv.novabank;

import org.alopsalv.novabank.model.Cliente;
import org.alopsalv.novabank.model.Cuenta;
import org.alopsalv.novabank.repository.ClienteRepository;
import org.alopsalv.novabank.repository.CuentaRepository;
import org.alopsalv.novabank.repository.MovimientoRepository;
import org.alopsalv.novabank.service.ClienteService;
import org.alopsalv.novabank.service.CuentaService;
import org.alopsalv.novabank.service.MovimientoService;

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
                    } while (opcionConsultas !=4);
                    break;
            }

        }while (opcionPrincipal !=5);

        System.out.println("Gracias por usar NovaBank :)");
        scanner.close();
    }
}
