import br.infuse.application.controller.PedidosController;
import br.infuse.application.dto.request.Pedido;
import br.infuse.application.dto.response.ServiceResponse;
import br.infuse.application.dto.response.SuccessResponse;
import br.infuse.application.enuns.Mensagens;
import br.infuse.application.service.ArquivosService;
import br.infuse.application.service.PedidosService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.FileInputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
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

    private static final Long PEDIDON = 0L;
    private static final LocalDate REGISTRON = null;
    private static final Long CLIENTEN = 0L;
    private static final String PRODUTON = "";
    private static final Double VALORN = 0.0d;
    private static final Integer QTDEN = 0;

    private static final String NMARQUIVOJSON = "src/test/java/resource/pedidos.json";
    private static final String NMARQUIVOXML = "src/test/java/resource/pedidos.xml";
    private static final String NMARQUIVOINVALID = "src/test/java/resource/pedidos.txt";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testReceiveXMLSuccess() throws Exception {

        InputStream inputStream = new FileInputStream(NMARQUIVOXML);
        assertNotNull(inputStream, "InputStream should not be null");
        MockMultipartFile file = new MockMultipartFile("arquivo", NMARQUIVOXML, MediaType.APPLICATION_XML_VALUE, inputStream);

        List<Pedido> pedidos = Collections.singletonList(new Pedido());

        when(arquivos.checkTipoDocument(file)).thenReturn(1);
        when(arquivos.arquivoToEntidade(file)).thenReturn(pedidos);
        when(service.cadastrarPedido(pedidos)).thenReturn(
                ServiceResponse.builder().status(true)
                        .mensagem(Mensagens.SUCCESS_MSG_IMPORT_FILE.value())
                        .dados(null).build());

        ResponseEntity<SuccessResponse> responseEntity = controller.receber(file);

        assertEquals(200, Objects.requireNonNull(responseEntity.getBody()).getCode());
        assertTrue(responseEntity.getBody().isSuccess());
        assertEquals(Mensagens.SUCCESS_MSG_IMPORT_FILE.value(), responseEntity.getBody().getMessage());
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void testReceiveJSONSuccess() throws Exception {
        InputStream inputStream = new FileInputStream(NMARQUIVOJSON);
        assertNotNull(inputStream, "InputStream should not be null");
        MockMultipartFile file = new MockMultipartFile("arquivo", NMARQUIVOJSON, MediaType.APPLICATION_JSON_VALUE, inputStream);

        List<Pedido> pedidos = Collections.singletonList(new Pedido());

        when(arquivos.checkTipoDocument(file)).thenReturn(1);
        when(arquivos.arquivoToEntidade(file)).thenReturn(pedidos);
        when(service.cadastrarPedido(pedidos)).thenReturn(
                ServiceResponse.builder().status(true)
                        .mensagem(Mensagens.SUCCESS_MSG_IMPORT_FILE.value())
                        .dados(null).build());

        ResponseEntity<SuccessResponse> responseEntity = controller.receber(file);

        assertEquals(200, Objects.requireNonNull(responseEntity.getBody()).getCode());
        assertTrue(responseEntity.getBody().isSuccess());
        assertEquals(Mensagens.SUCCESS_MSG_IMPORT_FILE.value(), responseEntity.getBody().getMessage());
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void testReceberComArquivoInvalido(){
        MockMultipartFile arquivo = new MockMultipartFile("arquivo", NMARQUIVOINVALID,
                MediaType.TEXT_PLAIN_VALUE, Mensagens.ERROR_INVALID_FILE.value().getBytes());
        when(arquivos.checkTipoDocument(any(MultipartFile.class))).thenReturn(0);
        assertThrows(EntityNotFoundException.class, () -> controller.receber(arquivo));
    }

    @Test
    void testConsultarPedidoSuccess() {

        ServiceResponse responseServ = ServiceResponse.builder()
                .status(true)
                .mensagem(Mensagens.SUCCESS_LIST_ORDERS.value())
                .dados(null).build();

        when(service.consultarPedido(PEDIDO, REGISTRO, CLIENTE, PRODUTO, VALOR, QTDE))
                .thenReturn(responseServ);

        ResponseEntity<SuccessResponse> response = controller.consultar(PEDIDO, REGISTRO, CLIENTE, PRODUTO, VALOR, QTDE);

        assert(response.getStatusCode().equals(HttpStatus.OK));
        assert(Objects.requireNonNull(response.getBody()).getMessage().equals(Mensagens.SUCCESS_LIST_ORDERS.value()));
    }

    @Test
    void testConsultarPedido_Error() {
        when(service.consultarPedido(anyLong(), any(), anyLong(), anyString(), anyDouble(), anyInt()))
                .thenThrow(new EntityNotFoundException(Mensagens.ERROR_LIST_NOT_FOUND.value()));

        assertThrows(EntityNotFoundException.class, () -> controller.consultar(PEDIDON, REGISTRON, CLIENTEN, PRODUTON, VALORN, QTDEN));
    }
}
