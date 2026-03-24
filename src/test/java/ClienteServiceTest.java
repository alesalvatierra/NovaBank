import org.alopsalv.novabank.model.Cliente;
import org.alopsalv.novabank.repository.ClienteRepository;
import org.alopsalv.novabank.service.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

class ClienteServiceTest {

    private ClienteService clienteService;
    private ClienteRepository clienteRepository;

    @BeforeEach
    void setUp() {
        //Inicializamos antes de cada test
        clienteRepository = new ClienteRepository();
        clienteService = new ClienteService(clienteRepository);
    }

    @Test
    @DisplayName("Debería de crear un cliente correctamente")
    void crearClienteBien() {
        Cliente cliente = new Cliente("Alejandro", "López", "12345678Z", "alejandrolopez@email.com", "612345678");
        Cliente guardado = clienteService.crearCliente(cliente);
        //Comprobamos que el ID no es NULL y que el dni es igual al esperado
        assertNotNull(guardado.getId());
        assertEquals("12345678Z", guardado.getDni());
    }

    @Test
    @DisplayName("Debería de lanzar error si el email no tiene @")
    void crearClienteEmailInvalido() {
        Cliente cliente = new Cliente("Alejandro", "López", "12345678Z", "alejandrolopez_email.com", "612345678");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            clienteService.crearCliente(cliente);
        });
        //Comprobamos que lanza el mensaje de error correctamente
        assertEquals("El formato del email no es válido.", exception.getMessage());
    }

    @Test
    @DisplayName("Debería de lanzar error si el nombre está vacío")
    void crearClienteNombreVacio() {
        //Introducimos un string vacío en el nombre
        Cliente cliente = new Cliente("", "López", "12345678Z", "alejandro@email.com", "612345678");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            clienteService.crearCliente(cliente);
        });
        //Comprobamos que lanza el mensaje de error correctamente
        assertEquals("El nombre es obligatorio.", exception.getMessage());
    }

    @Test
    @DisplayName("Debería de lanzar error si el DNI tiene un formato incorrecto")
    void crearClienteDniInvalido() {
        //Introducimos un DNI sin letra
        Cliente cliente = new Cliente("Alejandro", "López", "12345678", "alejandro@email.com", "612345678");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            clienteService.crearCliente(cliente);
        });
        //Comprobamos que lanza el mensaje de error correctamente
        assertTrue(exception.getMessage().contains("Formato de DNI inválido"));
    }

    @Test
    @DisplayName("Debería de crear un cliente con prefijo +34 en el teléfono")
    void crearClienteTelefonoConPrefijo() {
        // Comprobamos que el Regex del teléfono deja pasar el +34
        Cliente cliente = new Cliente("Alejandro", "López", "12345678Z", "alejandro@email.com", "+34612345678");
        Cliente guardado = clienteService.crearCliente(cliente);

        assertEquals("+34612345678", guardado.getTelefono());
    }

    @Test
    @DisplayName("Debería lanzar error si intentamos guardar un DNI duplicado")
    void crearClienteDniDuplicado() {
        Cliente cliente1 = new Cliente("Alejandro", "López", "12345678Z", "alejandro@email.com", "612345678");
        clienteService.crearCliente(cliente1);

        //Intentamos guardar otro cliente con el mismo DNI
        Cliente cliente2 = new Cliente("Javier", "Lanzas", "12345678Z", "javier@email.com", "687654321");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            clienteService.crearCliente(cliente2);
        });

        assertTrue(exception.getMessage().contains("Ya existe un cliente con el DNI"));
    }

    @Test
    @DisplayName("Debería buscar y encontrar a un cliente por su DNI")
    void buscarClientePorDniBien() {

        Cliente cliente = new Cliente("Alejandro", "López", "12345678Z", "alejandro@email.com", "612345678");
        clienteService.crearCliente(cliente);

        //Buscamos al cliente
        Cliente encontrado = clienteService.buscarClientePorDni("12345678Z");

        //Comprobamos que no es nulo y que el nombre coincide
        assertNotNull(encontrado);
        assertEquals("Alejandro", encontrado.getNombre());
    }
}