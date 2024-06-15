package services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import br.infuse.application.dto.request.ClienteDTO;
import br.infuse.application.dto.response.ServiceResponse;
import br.infuse.application.model.Clientes;
import br.infuse.application.repository.IClientesRepository;
import br.infuse.application.service.ClientesService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class ClientesServiceTest {

    @Mock
    private IClientesRepository repository;

    @InjectMocks
    private ClientesService service;

    private ClienteDTO clienteDTO;
    private Clientes cliente;

    @BeforeEach
    void setUp() {
        clienteDTO = ClienteDTO.builder()
                .nome("Nome Cliente")
                .email("email@cliente.com")
                .telefone("123456789").build();

        cliente = new Clientes();
        cliente.setId(1L);
        cliente.setNomeCliente("Nome Cliente");
        cliente.setEmailClient("email@cliente.com");
        cliente.setPhoneClient("123456789");
        cliente.setDtUpdate(LocalDateTime.now());
    }

    @Test
    void ClientesService_CreatePokemon_ReturnsResponseDto() {
        when(repository.save(Mockito.any(Clientes.class))).thenReturn(cliente);

        ServiceResponse response = service.cadastrarClientes(clienteDTO);

        Assertions.assertThat(response).isNotNull();
    }

    @Test
    void ClientesService_GetAllClientes_ReturnsResponseDto() {
        Page<Clientes> clientes = Mockito.mock(Page.class);

        when(repository.findAll(Mockito.any(Pageable.class))).thenReturn(clientes);

        ServiceResponse response = service.consultarClientes(1,10);

        Assertions.assertThat(response).isNotNull();
    }

    @Test
    void ClientesService_FindById_ReturnResponseDto() {
        long id = 1;
        when(repository.findById(id)).thenReturn(Optional.ofNullable(cliente));

        ServiceResponse response = service.consultarClientePorId(id);

        Assertions.assertThat(response).isNotNull();
    }

    @Test
    void ClientesService_UpdatePokemon_ReturnPokemonDto() {
        long id = 1;
        when(repository.findById(id)).thenReturn(Optional.ofNullable(cliente));
        when(repository.save(cliente)).thenReturn(cliente);

        ServiceResponse response = service.atualizarClientes(clienteDTO, id);

        Assertions.assertThat(response).isNotNull();
    }

    @Test
    void ClientesService_DeletePokemonById_ReturnVoid() {
        long id = 1;

        when(repository.findById(id)).thenReturn(Optional.ofNullable(cliente));
        doNothing().when(repository).delete(cliente);

        assertAll(() -> service.deletarClientes(id));
    }
}
