package org.alopsalv.novabank;

import org.alopsalv.novabank.model.*;
import org.alopsalv.novabank.repository.ClienteRepository;
import org.alopsalv.novabank.repository.CuentaRepository;
import org.alopsalv.novabank.repository.MovimientoRepository;
import org.alopsalv.novabank.service.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) {
        ClienteRepository clienteRepo = new ClienteRepository();
        CuentaRepository cuentaRepo = new CuentaRepository();
        MovimientoRepository movimientoRepo = new MovimientoRepository();

        ClienteService clienteService = new ClienteService(clienteRepo);
        CuentaService cuentaService = new CuentaService(cuentaRepo, clienteService);
        MovimientoService movimientoService = new MovimientoService(movimientoRepo);

        Scanner scanner = new Scanner(System.in);
        int opcionPrincipal = -1;

        do {
            //Menú principal
            System.out.println("====================================");
            System.out.println("NOVABANK - SISTEMA DE OPERACIONES");
            System.out.println("====================================");
            System.out.println("1. Gestión de clientes\n2. Gestión de cuentas\n3. Operaciones financieras\n4. Consultas\n5. Salir\n ");
            System.out.print("Seleccione una opción: ");
            opcionPrincipal = leerEntero(scanner);

            switch (opcionPrincipal) {
                case 1 -> menuClientes(scanner, clienteService);
                case 2 -> menuCuentas(scanner, clienteService, cuentaService);
                case 3 -> menuOperaciones(scanner, cuentaService, movimientoService);
                case 4 -> menuConsultas(scanner, cuentaService, movimientoService);
            }
        } while (opcionPrincipal != 5);

        System.out.println("Gracias por usar NovaBank :)");
        scanner.close();
    }

    //Submenús

    private static void menuClientes(Scanner sc, ClienteService service) {
        int opcion = -1;
        do {
            System.out.println("--- GESTIÓN DE CLIENTES ---");
            System.out.println("1. Crear cliente\n2. Buscar cliente\n3. Listar clientes\n4. Volver");
            opcion = leerEntero(sc);
            switch (opcion) {
                case 1 -> crearCliente(sc, service);
                case 2 -> buscarCliente(sc, service);
                case 3 -> listarClientes(service);
            }
        } while (opcion != 4);
    }

    private static void menuCuentas(Scanner sc, ClienteService clS, CuentaService cuS) {
        int opcion = -1;
        do {
            System.out.println("--- GESTIÓN DE CUENTAS ---");
            System.out.println("1. Crear cuenta\n2. Listar cuentas de cliente\n3. Ver información de cuenta\n4. Volver");
            opcion = leerEntero(sc);
            switch (opcion) {
                case 1 -> crearCuenta(sc, clS, cuS);
                case 2 -> listarCuentasCliente(sc, clS, cuS);
                case 3 -> verInfoCuenta(sc, clS, cuS);
            }
        } while (opcion != 4);
    }

    private static void menuOperaciones(Scanner sc, CuentaService cuS, MovimientoService moS) {
        int opcion = -1;
        do {
            System.out.println("--- OPERACIONES FINANCIERAS ---");
            System.out.println("1. Depositar dinero\n2. Retirar dinero\n3. Transferencia entre cuentas\n4. Volver");
            opcion = leerEntero(sc);
            switch (opcion) {
                case 1 -> depositar(sc, cuS, moS);
                case 2 -> retirar(sc, cuS, moS);
                case 3 -> transferir(sc, cuS, moS);
            }
        } while (opcion != 4);
    }

    private static void menuConsultas(Scanner sc, CuentaService cuS, MovimientoService moS) {
        int opcion = -1;
        do {
            System.out.println("--- CONSULTAS ---");
            System.out.println("1. Consultar saldo\n2. Historial de movimientos\n3. Movimientos por rango de fechas\n4. Volver");
            opcion = leerEntero(sc);
            switch (opcion) {
                case 1 -> consultarSaldo(sc, cuS);
                case 2 -> historial(sc, cuS, moS);
                case 3 -> historialPorFechas(sc, cuS, moS);
            }
        } while (opcion != 4);
    }

    //Lógica de ejecución
    private static void crearCliente(Scanner sc, ClienteService service) {
        System.out.println("--- CREACIÓN DE CLIENTE ---");
        System.out.print("Introduzca el nombre: "); String n = sc.nextLine();
        System.out.print("Introduzca los apellidos: "); String a = sc.nextLine();
        System.out.print("Introduzca el DNI/NIF: "); String d = sc.nextLine();
        System.out.print("Introduzca el email: "); String e = sc.nextLine();
        System.out.print("Introduzca el teléfono: "); String t = sc.nextLine();
        try {
            Cliente nuevo = service.crearCliente(new Cliente(n, a, d, e, t));
            System.out.println("Cliente creado correctamente.");
            System.out.println("ID cliente: " + nuevo.getId());
        } catch (IllegalArgumentException ex) { System.err.println("ERROR: " + ex.getMessage()); }
    }

    private static void buscarCliente(Scanner sc, ClienteService service) {
        System.out.println("--- BUSCAR CLIENTE ---");
        System.out.println("1. Por ID interno\n2. Por DNI");
        System.out.print("Elija criterio de búsqueda: ");
        int modo = leerEntero(sc);
        Cliente c = null;
        if (modo == 1) {
            System.out.print("Introduzca el ID del cliente: ");
            c = service.buscarCliente(leerLong(sc));
        } else if (modo == 2) {
            System.out.print("Introduzca el DNI del cliente: ");
            c = service.buscarClientePorDni(sc.nextLine());
        }
        if (c != null) {
            System.out.println("Cliente encontrado: ");
            System.out.println("ID: " + c.getId() + "\nNombre: " + c.getNombre() + "\nDNI: " + c.getDni() + "\nEmail: " + c.getEmail() + "\nTeléfono: " + c.getTelefono());
        } else if (modo == 1 || modo == 2) { System.out.println("No se encontró ningún cliente con esos datos."); }
    }

    private static void listarClientes(ClienteService service) {
        System.out.println("--- LISTADO DE CLIENTES ---");
        List<Cliente> lista = service.obtenerTodosLosClientes();
        if (lista.isEmpty()) { System.out.println("No hay clientes registrados en el sistema."); return; }
        System.out.printf("%-5s | %-35s | %-10s | %-35s | %-10s%n", "ID", "Nombre", "DNI", "Email", "Teléfono");
        System.out.println("------|-------------------------------------|------------|-------------------------------------|-----------");
        for (Cliente c : lista) {
            System.out.printf("%-5d | %-35s | %-10s | %-35s | %-10s%n", c.getId(), c.getNombre() + " " + c.getApellidos(), c.getDni(), c.getEmail(), c.getTelefono());
        }
    }

    private static void crearCuenta(Scanner sc, ClienteService clS, CuentaService cuS) {
        System.out.println("--- CREAR CUENTA ---");
        System.out.print("Introduzca el ID del cliente titular: ");
        Long id = leerLong(sc);
        Cliente t = clS.buscarCliente(id);
        if (t == null) { System.err.println("ERROR: No existe ningún cliente registrado con el ID " + id); return; }
        Cuenta c = new Cuenta();
        c.setClienteId(id); c.setSaldo(BigDecimal.ZERO); c.setFechaCreacion(LocalDateTime.now());
        try {
            Cuenta guardada = cuS.crearCuenta(c);
            String iban = String.format("ES91210000%012d", guardada.getId());
            guardada.setNumeroCuenta(iban);
            System.out.println("Cuenta creada correctamente.\nNúmero de cuenta: " + iban);
            System.out.println("Titular: " + t.getNombre() + " " + t.getApellidos() + " (ID: " + t.getId() + ")\nSaldo inicial: 0,00 €");
        } catch (Exception ex) { System.err.println("ERROR: " + ex.getMessage()); }
    }

    private static void listarCuentasCliente(Scanner sc, ClienteService clS, CuentaService cuS) {
        System.out.println("--- LISTAR CUENTAS DEL CLIENTE ---");
        System.out.print("Introduzca ID del cliente: ");
        Long id = leerLong(sc);
        Cliente t = clS.buscarCliente(id);
        if (t == null) { System.err.println("ERROR: No existe ningún cliente registrado con el ID " + id); return; }
        List<Cuenta> cuentas = cuS.obtenerCuentasDeCliente(id);
        System.out.println("Cuentas del cliente " + t.getNombre() + " " + t.getApellidos() + ":");
        if (cuentas.isEmpty()) { System.out.println("Este cliente aún no tiene cuentas bancarias abiertas."); }
        else {
            System.out.printf("%-25s | %-15s%n", "Número de cuenta", "Saldo");
            System.out.println("--------------------------|----------------");
            for (Cuenta c : cuentas) { System.out.printf("%-25s | %s €%n", c.getNumeroCuenta(), c.getSaldo()); }
        }
    }

    private static void verInfoCuenta(Scanner sc, ClienteService clS, CuentaService cuS) {
        System.out.println("--- INFORMACIÓN DE CUENTA ---");
        System.out.print("Introduzca número de cuenta: ");
        Cuenta c = cuS.buscarCuenta(sc.nextLine());
        if (c == null) { System.err.println("ERROR: No se encontró ninguna cuenta."); return; }
        Cliente t = clS.buscarCliente(c.getClienteId());
        System.out.println("Número de cuenta: " + c.getNumeroCuenta());
        System.out.println("Titular: " + (t != null ? t.getNombre() + " " + t.getApellidos() : "Desconocido"));
        System.out.println("Saldo: " + c.getSaldo() + " €\nFecha de creación: " + c.getFechaCreacion().format(FMT));
    }

    private static void depositar(Scanner sc, CuentaService cuS, MovimientoService moS) {
        System.out.println("--- DEPOSITAR DINERO ---");
        System.out.print("Introduzca el número de cuenta (IBAN): ");
        Cuenta c = cuS.buscarCuenta(sc.nextLine());
        if (c == null) { System.err.println("ERROR: Cuenta no encontrada."); return; }
        System.out.print("Introduzca la cantidad a depositar: ");
        BigDecimal cant = leerBigDecimal(sc);
        if (cant.compareTo(BigDecimal.ZERO) <= 0) { System.err.println("ERROR: Cantidad debe ser mayor que 0"); return; }
        moS.registrarMovimiento(crearM(c.getId(), "DEPOSITO", cant), c);
        System.out.println("Depósito realizado correctamente.\nCuenta: " + c.getNumeroCuenta());
        System.out.printf("Importe: +%,.2f €%nNuevo saldo: %,.2f €%n", cant, c.getSaldo());
    }

    private static void retirar(Scanner sc, CuentaService cuS, MovimientoService moS) {
        System.out.println("--- RETIRAR DINERO ---");
        System.out.print("Introduzca el número de cuenta (IBAN): ");
        Cuenta c = cuS.buscarCuenta(sc.nextLine());
        if (c == null) { System.err.println("ERROR: Cuenta no encontrada."); return; }
        System.out.print("Introduzca la cantidad a retirar: ");
        BigDecimal cant = leerBigDecimal(sc);
        if (cant.compareTo(BigDecimal.ZERO) <= 0 || c.getSaldo().compareTo(cant) < 0) {
            System.err.println("ERROR: Saldo insuficiente o cantidad inválida."); return;
        }
        moS.registrarMovimiento(crearM(c.getId(), "RETIRADA", cant), c);
        System.out.println("Retirada realizada correctamente.\nCuenta: " + c.getNumeroCuenta());
        System.out.printf("Importe: -%,.2f €%nNuevo saldo: %,.2f €%n", cant, c.getSaldo());
    }

    private static void transferir(Scanner sc, CuentaService cuS, MovimientoService moS) {
        System.out.println("--- TRANSFERENCIAS ENTRE CUENTAS ---");
        System.out.print("IBAN ORIGEN: "); Cuenta ori = cuS.buscarCuenta(sc.nextLine());
        if (ori == null) { System.err.println("ERROR: Origen no encontrado."); return; }
        System.out.print("IBAN DESTINO: "); Cuenta des = cuS.buscarCuenta(sc.nextLine());
        if (des == null) { System.err.println("ERROR: Destino no encontrado."); return; }
        if (ori.getId().equals(des.getId())) { System.err.println("ERROR: Misma cuenta."); return; }
        System.out.print("Cantidad: ");
        BigDecimal cant = leerBigDecimal(sc);
        if (cant.compareTo(BigDecimal.ZERO) <= 0 || ori.getSaldo().compareTo(cant) < 0) {
            System.err.println("ERROR: Saldo insuficiente."); return;
        }
        moS.registrarMovimiento(crearM(ori.getId(), "TRANSFERENCIA_SALIENTE", cant), ori);
        moS.registrarMovimiento(crearM(des.getId(), "TRANSFERENCIA_ENTRANTE", cant), des);
        System.out.println("Transferencia realizada correctamente.");
        System.out.printf("Cuenta origen: %s → -%,.2f €%nCuenta destino: %s → +%,.2f €%n", ori.getNumeroCuenta(), cant, des.getNumeroCuenta(), cant);
    }

    private static void consultarSaldo(Scanner sc, CuentaService cuS) {
        System.out.println("--- CONSULTAR SALDO ---");
        System.out.print("IBAN: ");
        Cuenta c = cuS.buscarCuenta(sc.nextLine());
        if (c == null) { System.err.println("ERROR: No existe."); return; }
        System.out.println("Consulta de saldo exitosa.\nCuenta: " + c.getNumeroCuenta());
        System.out.printf("Saldo disponible: %,.2f €%n", c.getSaldo());
    }

    private static void historial(Scanner sc, CuentaService cuS, MovimientoService moS) {
        System.out.println("---HISTORIAL DE MOVIMIENTOS---");
        System.out.print("IBAN: ");
        Cuenta c = cuS.buscarCuenta(sc.nextLine());
        if (c == null) { System.err.println("ERROR: No existe."); return; }
        List<Movimiento> ms = moS.obtenerMovimientosDeCuenta(c.getId());
        if (ms.isEmpty()) { System.out.println("No hay movimientos."); }
        else {
            System.out.println("Historial de la cuenta: " + c.getNumeroCuenta());
            System.out.printf("%-5s | %-20s | %-25s | %-15s%n", "ID", "Fecha", "Tipo", "Importe");
            System.out.println("------|----------------------|---------------------------|----------------");
            for (Movimiento m : ms) {
                String s = (m.getTipo().equals("DEPOSITO") || m.getTipo().equals("TRANSFERENCIA_ENTRANTE")) ? "+" : "-";
                System.out.printf("%-5d | %-20s | %-25s | %s%,.2f €%n", m.getId(), m.getFecha().format(FMT), m.getTipo(), s, m.getCantidad());
            }
        }
    }

    private static void historialPorFechas(Scanner sc, CuentaService cuS, MovimientoService moS) {
        System.out.println("--- MOVIMIENTOS POR RANGO DE FECHAS ---");
        System.out.print("IBAN: ");
        Cuenta c = cuS.buscarCuenta(sc.nextLine());
        if (c == null) { System.err.println("ERROR: No existe."); return; }
        try {
            System.out.print("Fecha inicio (AAAA-MM-DD): "); LocalDate ini = LocalDate.parse(sc.nextLine());
            System.out.print("Fecha fin (AAAA-MM-DD): "); LocalDate fin = LocalDate.parse(sc.nextLine());
            List<Movimiento> f = moS.obtenerMovimientosPorFecha(c.getId(), ini, fin);
            if (f.isEmpty()) { System.out.println("No se han encontrado movimientos."); }
            else {
                System.out.println("Extracto desde " + ini + " hasta " + fin + ":");
                System.out.printf("%-5s | %-20s | %-25s | %-15s%n", "ID", "Fecha", "Tipo", "Importe");
                System.out.println("------|----------------------|---------------------------|----------------");
                for (Movimiento m : f) {
                    String s = (m.getTipo().contains("DEPOSITO") || m.getTipo().contains("ENTRANTE")) ? "+" : "-";
                    System.out.printf("%-5d | %-20s | %-25s | %s%,.2f €%n", m.getId(), m.getFecha().format(FMT), m.getTipo(), s, m.getCantidad());
                }
            }
        } catch (Exception ex) { System.err.println("ERROR: Formato AAAA-MM-DD"); }
    }

    //Controla las opciones del menú
    private static int leerEntero(Scanner sc) { try { return Integer.parseInt(sc.nextLine()); } catch (Exception ex) { return -1; } }
    //Controla los ID
    private static Long leerLong(Scanner sc) { try { return Long.parseLong(sc.nextLine()); } catch (Exception ex) { return -1L; } }
    //Controla los BigDecimal
    private static BigDecimal leerBigDecimal(Scanner sc) { try { return new BigDecimal(sc.nextLine()); } catch (Exception ex) { return BigDecimal.ZERO; } }
    //Método factoría para crear el Movimiento
    private static Movimiento crearM(Long id, String t, BigDecimal c) {
        Movimiento m = new Movimiento(); m.setCuentaId(id); m.setTipo(t); m.setCantidad(c); m.setFecha(LocalDateTime.now()); return m;
    }
}