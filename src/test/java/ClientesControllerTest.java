import br.infuse.application.controller.ClientesController;
import br.infuse.application.dto.request.Cliente;
import br.infuse.application.dto.request.Clientes;
import br.infuse.application.dto.response.ServiceResponse;
import br.infuse.application.dto.response.SuccessResponse;
import br.infuse.application.enuns.Mensagens;
import br.infuse.application.service.ClientesService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.persistence.EntityNotFoundException;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientesControllerTest {

    private static final String NOME = "cliente";
    private static final String NOMETESTE = "teste";
    private static final Long ID = 1L;
    private static final Long IDNULL = null;

    private static final String CLIENTETESTE = "Cliente teste";
    private static final Long IDTESTE = 10L;

    @Mock
    private ClientesService service;

    @InjectMocks
    private ClientesController controller;

    @Test
    void testConsultarClienteSuccess() {
        ServiceResponse responseServ = ServiceResponse.builder()
                .status(true)
                .mensagem(Mensagens.SUCCESS_LIST_CLIENTS.value())
                .dados(null).build();

        when(service.consultarClientes(NOME, ID))
                .thenReturn(responseServ);

        ResponseEntity<SuccessResponse> response = controller.consultar(NOME, ID);

        assert(response.getStatusCode().equals(HttpStatus.OK));
        assert(Objects.requireNonNull(response.getBody()).getMessage().equals(Mensagens.SUCCESS_LIST_CLIENTS.value()));
    }

    @Test
    void testConsultarClienteError() {
        when(service.consultarClientes(NOMETESTE, IDNULL))
                .thenThrow(new EntityNotFoundException(Mensagens.ERROR_LIST_NOT_FOUND.value()));

        assertThrows(EntityNotFoundException.class, () -> controller.consultar(NOMETESTE, IDNULL));
    }

    @Test
    void testCadastrarClienteSuccess() {
        ServiceResponse responseServ = ServiceResponse.builder()
                .status(true)
                .mensagem(Mensagens.SUCCESS_SAVE_CLIENT.value())
                .dados(null).build();

        Clientes clientes = new Clientes();
        List<Cliente> clientelista = Collections.singletonList(Cliente.builder().nome(CLIENTETESTE).id(IDTESTE).build());
        clientes.setClientes(clientelista);

        when(service.cadastrarClientes(clientes))
                .thenReturn(responseServ);

        ResponseEntity<SuccessResponse> response = controller.cadastrar(clientes);

        assert(response.getStatusCode().equals(HttpStatus.OK));
        assert(Objects.requireNonNull(response.getBody()).getMessage().equals(Mensagens.SUCCESS_SAVE_CLIENT.value()));
    }

    @Test
    void testCadastrarClienteError() {

        when(service.cadastrarClientes(any()))
                .thenThrow(new EntityNotFoundException(Mensagens.ERROR_SAVE_CLIENT.value()));
        assertThrows(EntityNotFoundException.class, () -> controller.cadastrar(new Clientes()));
    }
}

