import br.infuse.application.controller.ClientesController;
import br.infuse.application.dto.request.Clientes;
import br.infuse.application.dto.response.ServiceResponse;
import br.infuse.application.dto.response.SuccessResponse;
import br.infuse.application.enuns.Mensagens;
import br.infuse.application.service.ClientesService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.persistence.EntityNotFoundException;

import java.util.Objects;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@Disabled
class ClientesControllerTest {

    private static final String NOME = "cliente";
    private static final Long ID = 1L;
    private static final Long IDNULL = null;

    @Mock
    private ClientesService service;

    @InjectMocks
    private ClientesController controller;

    @Test
    void testConsultarCliente_Success() {
        ServiceResponse responseServ = ServiceResponse.builder()
                .status(true)
                .mensagem(Mensagens.SUCCESS_LIST_CLIENTS.value())
                .dados(null).build();

        when(service.consultarClientes(anyString(), anyLong()))
                .thenReturn(responseServ);

        ResponseEntity<SuccessResponse> response = controller.consultar(NOME, ID);

        assert(response.getStatusCode().equals(HttpStatus.OK));
        assert(Objects.requireNonNull(response.getBody()).getMessage().equals(Mensagens.SUCCESS_LIST_CLIENTS.value()));
    }

    @Test
    void testConsultarCliente_EntityNotFound() {
        when(service.consultarClientes(anyString(), anyLong()))
                .thenThrow(new EntityNotFoundException(Mensagens.ERROR_LIST_NOT_FOUND.value()));

        ResponseEntity<SuccessResponse> response = controller.consultar(NOME, IDNULL);

        assert(response.getStatusCode().equals(HttpStatus.NOT_FOUND));
        assert(Objects.requireNonNull(response.getBody()).getMessage().equals(Mensagens.ERROR_LIST_NOT_FOUND.value()));
    }

    @Test
    void testCadastrarCliente_Success() {
        ServiceResponse responseServ = ServiceResponse.builder()
                .status(true)
                .mensagem(Mensagens.SUCCESS_SAVE_CLIENT.value())
                .dados(null).build();

        when(service.cadastrarClientes(any()))
                .thenReturn(responseServ);

        ResponseEntity<SuccessResponse> response = controller.cadastrar(new Clientes());

        assert(response.getStatusCode().equals(HttpStatus.OK));
        assert(Objects.requireNonNull(response.getBody()).getMessage().equals(Mensagens.SUCCESS_SAVE_CLIENT.value()));
    }

    @Test
    void testCadastrarCliente_EntityNotFound() {
        when(service.cadastrarClientes(any()))
                .thenThrow(new EntityNotFoundException(Mensagens.ERROR_SAVE_CLIENT.value()));

        ResponseEntity<SuccessResponse> response = controller.cadastrar(new Clientes());

        assert(response.getStatusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR));
        assert(Objects.requireNonNull(response.getBody()).getMessage().equals(Mensagens.ERROR_SAVE_CLIENT.value()));
    }
}
