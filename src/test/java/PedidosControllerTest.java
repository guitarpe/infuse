import br.infuse.application.controller.PedidosController;
import br.infuse.application.dto.request.Pedido;
import br.infuse.application.dto.response.ServiceResponse;
import br.infuse.application.dto.response.SuccessResponse;
import br.infuse.application.enuns.Mensagens;
import br.infuse.application.service.ArquivosService;
import br.infuse.application.service.PedidosService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@Disabled
class PedidosControllerTest {
    @Mock
    private PedidosService service;

    @Mock
    private ArquivosService arquivos;

    @InjectMocks
    private PedidosController controller;

    private static final Long PEDIDO = 123L;
    private static final LocalDate REGISTRO = LocalDate.now();
    private static final Long CLIENTE = 456L;
    private static final String PRODUTO = "Produto de Exemplo";
    private static final Double VALOR = 10.0;
    private static final Integer QTDE = 2;

    private static final Long PEDIDON = null;
    private static final LocalDate REGISTRON = null;
    private static final Long CLIENTEN = null;
    private static final String PRODUTON = null;
    private static final Double VALORN = null;
    private static final Integer QTDEN = null;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testReceiveXML() throws Exception {
        Resource resource = new ClassPathResource("pedidos.xml");
        InputStream inputStream = resource.getInputStream();
        MockMultipartFile file = new MockMultipartFile("arquivo", "pedidos.xml", "application/xml", inputStream);

        List<Pedido> pedidos = arquivos.arquivoToEntidade(file);

        when(arquivos.arquivoToEntidade(file)).thenReturn(pedidos);
        when(service.cadastrarPedido(any())).thenReturn(
                ServiceResponse.builder().status(true)
                .mensagem(Mensagens.SUCCESS_MSG_IMPORT_FILE.value())
                .dados(null).build());

        ResponseEntity<SuccessResponse> responseEntity = controller.cadastrar(file);

        assertEquals(200, Objects.requireNonNull(responseEntity.getBody()).getCode());
        assertTrue(responseEntity.getBody().isSuccess());
        assertEquals(Mensagens.SUCCESS_MSG_IMPORT_FILE.value(), responseEntity.getBody().getMessage());
    }

    @Test
    void testReceiveJSON() throws Exception {
        Resource resource = new ClassPathResource("pedidos.json");
        InputStream inputStream = resource.getInputStream();
        MockMultipartFile file = new MockMultipartFile("arquivo", "pedidos.json", "application/json", inputStream);

        List<Pedido> pedidos = arquivos.arquivoToEntidade(file);

        when(arquivos.arquivoToEntidade(file)).thenReturn(pedidos);
        when(service.cadastrarPedido(any())).thenReturn(
                ServiceResponse.builder().status(true)
                        .mensagem(Mensagens.SUCCESS_MSG_IMPORT_FILE.value())
                        .dados(null).build());

        ResponseEntity<SuccessResponse> responseEntity = controller.cadastrar(file);

        assertEquals(200, Objects.requireNonNull(responseEntity.getBody()).getCode());
        assertTrue(responseEntity.getBody().isSuccess());
        assertEquals(Mensagens.SUCCESS_MSG_IMPORT_FILE.value(), responseEntity.getBody().getMessage());
    }

    @Test
    void testConsultarPedido_Success() {

        ServiceResponse response = ServiceResponse.builder()
                .status(true)
                .mensagem(Mensagens.SUCCESS_LIST_ORDERS.value())
                .dados(null).build();

        when(service.consultarPedido(PEDIDO, REGISTRO, CLIENTE, PRODUTO, VALOR, QTDE))
                .thenReturn(response);

        ResponseEntity<SuccessResponse> responseEntity = controller.consultar(PEDIDO, REGISTRO, CLIENTE, PRODUTO, VALOR, QTDE);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(200, Objects.requireNonNull(responseEntity.getBody()).getCode());
        assertEquals(Mensagens.SUCCESS_LIST_ORDERS.value(), responseEntity.getBody().getMessage());

        verify(service).consultarPedido(PEDIDO, REGISTRO, CLIENTE, PRODUTO, VALOR, QTDE);
    }

    @Test
    void testConsultarPedido_EntityNotFound() {
        when(service.consultarPedido(anyLong(), any(LocalDate.class), anyLong(), anyString(), anyDouble(), anyInt()))
                .thenThrow(new EntityNotFoundException(Mensagens.ERROR_LIST_NOT_FOUND.value()));

        ResponseEntity<SuccessResponse> response = controller.consultar(PEDIDON, REGISTRON, CLIENTEN, PRODUTON, VALORN, QTDEN);

        assert(response.getStatusCode().equals(HttpStatus.NOT_FOUND));
        assert(Objects.requireNonNull(response.getBody()).getMessage().equals(Mensagens.ERROR_LIST_NOT_FOUND.value()));
    }
}
