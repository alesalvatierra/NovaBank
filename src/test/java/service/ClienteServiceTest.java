package service;

import org.alopsalv.novabank.model.Cliente;
import org.alopsalv.novabank.repository.ClienteRepository;
import org.alopsalv.novabank.service.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    @BeforeEach
    void setUp() {
        // Con Mockito ya no inicializamos manualmente con 'new'
        // Las anotaciones @Mock e @InjectMocks hacen el trabajo por nosotros
    }

    @Test
    @DisplayName("Debería de crear un cliente correctamente")
    void crearClienteBien() {
        Cliente cliente = new Cliente("Alejandro", "López", "12345678Z", "alejandrolopez@email.com", "612345678");

        //Simulamos que al guardar en la BD, se le asigna el ID 1000
        Cliente clienteGuardadoMock = new Cliente("Alejandro", "López", "12345678Z", "alejandrolopez@email.com", "612345678");
        clienteGuardadoMock.setId(1000L);
        when(clienteRepository.guardar(any(Cliente.class))).thenReturn(clienteGuardadoMock);

        Cliente guardado = clienteService.crearCliente(cliente);

        //Comprobamos que el ID no es NULL y que el dni es igual al esperado
        assertNotNull(guardado.getId());
        assertEquals("12345678Z", guardado.getDni());
        verify(clienteRepository).guardar(any(Cliente.class));
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
        //Nos aseguramos de que no intentó guardarlo en la BD
        verifyNoInteractions(clienteRepository);
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
        verifyNoInteractions(clienteRepository);
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
        verifyNoInteractions(clienteRepository);
    }

    @Test
    @DisplayName("Debería de crear un cliente con prefijo +34 en el teléfono")
    void crearClienteTelefonoConPrefijo() {
        Cliente cliente = new Cliente("Alejandro", "López", "12345678Z", "alejandro@email.com", "+34612345678");

        Cliente clienteGuardadoMock = new Cliente("Alejandro", "López", "12345678Z", "alejandro@email.com", "+34612345678");
        clienteGuardadoMock.setId(1001L);
        when(clienteRepository.guardar(any(Cliente.class))).thenReturn(clienteGuardadoMock);

        Cliente guardado = clienteService.crearCliente(cliente);

        assertEquals("+34612345678", guardado.getTelefono());
    }

    @Test
    @DisplayName("Debería lanzar error si intentamos guardar un DNI duplicado")
    void crearClienteDniDuplicado() {
        //Creamos el cliente de la base de datos
        Cliente clienteExistente = new Cliente("Alejandro", "López", "12345678Z", "alejandro@email.com", "612345678");
        java.util.List<Cliente> listaConCliente = java.util.Arrays.asList(clienteExistente);
        when(clienteRepository.listarTodos()).thenReturn(listaConCliente);

        //Intentamos guardar un segundo cliente con el mismo DNI
        Cliente cliente2 = new Cliente("Javier", "Lanzas", "12345678Z", "javier@email.com", "687654321");

        //Verificamos que salte el error
        assertThrows(IllegalArgumentException.class, () -> {
            clienteService.crearCliente(cliente2);
        });

        //Verificamos que NUNCA se llame a guardar
        verify(clienteRepository, never()).guardar(any());
    }

    @Test
    @DisplayName("Debería buscar y encontrar a un cliente por su DNI")
    void buscarClientePorDniBien() {
        Cliente clienteMock = new Cliente("Alejandro", "López", "12345678Z", "alejandro@email.com", "612345678");

        //Configuramos el mock para que devuelva nuestro cliente envuelto en un Optional
        when(clienteRepository.buscarPorDni("12345678Z")).thenReturn(Optional.of(clienteMock));

        //Buscamos al cliente
        Cliente encontrado = clienteService.buscarClientePorDni("12345678Z");

        //Comprobamos que no es nulo y que el nombre coincide
        assertNotNull(encontrado);
        assertEquals("Alejandro", encontrado.getNombre());
    }
}