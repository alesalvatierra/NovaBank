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
        assertEquals("El formato del email no es válido", exception.getMessage());
    }
}