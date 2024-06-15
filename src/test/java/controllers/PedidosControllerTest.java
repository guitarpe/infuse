package controllers;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import br.infuse.application.controller.PedidosController;
import br.infuse.application.dto.request.PedidoDTO;
import br.infuse.application.dto.response.PedidosRespose;
import br.infuse.application.dto.response.ServiceResponse;
import br.infuse.application.enuns.Mensagens;
import br.infuse.application.service.ArquivosService;
import br.infuse.application.service.PedidosService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.StreamUtils;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Collections;

@ExtendWith(MockitoExtension.class)
class PedidosControllerTest {

    @InjectMocks
    private PedidosController pedidosController;

    @Mock
    private ArquivosService arquivosService;

    @Mock
    private PedidosService service;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PedidoDTO pedido;

    @Autowired
    private ResourceLoader resourceLoader;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(pedidosController).build();
        pedido = new PedidoDTO();
        pedido.setControle(123L);
        pedido.setClientId(1L);
        pedido.setQuantidade(10);
        pedido.setProduto("produto 1");
        pedido.setValor(BigDecimal.valueOf(10.0));
        pedido.setDtRegistro("2024-06-14");
    }

    @Test
    void PedidosController_CreatePedido_XML_ReturnCreated() throws Exception {
        ServiceResponse response = ServiceResponse.builder().status(true)
                .mensagem(Mensagens.ORDER_SUCCESS_SAVE.value()).dados(Collections.singletonList(pedido)).build();

        ClassPathResource resource = new ClassPathResource("pedidos.xml");
        String xmlContent = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);

        MockMultipartFile file = new MockMultipartFile(
                "arquivo", "pedido.xml",
                MediaType.APPLICATION_XML_VALUE, xmlContent.getBytes());

        when(arquivosService.checkTipoDocument(any())).thenReturn(1);
        when(arquivosService.converterParaEntidade(any())).thenReturn(Collections.singletonList(new PedidoDTO()));
        when(service.cadastrarPedido(any())).thenReturn(response);

        mockMvc.perform(fileUpload("/api/pedidos/registrar")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", CoreMatchers.is(200)))
                .andExpect(jsonPath("$.success", CoreMatchers.is(true)))
                .andExpect(jsonPath("$.message", CoreMatchers.is(Mensagens.ORDER_SUCCESS_SAVE.value())))
                .andExpect(jsonPath("$.data.size()", CoreMatchers.is(Collections.singletonList(new PedidoDTO()).size())));
    }

    @Test
    void PedidosController_CreatePedido_JSON_ReturnCreated() throws Exception {
        ServiceResponse response = ServiceResponse.builder().status(true)
                .mensagem(Mensagens.ORDER_SUCCESS_SAVE.value()).dados(Collections.singletonList(pedido)).build();

        ClassPathResource resource = new ClassPathResource("pedidos.json");
        String jsonContent = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);

        MockMultipartFile file = new MockMultipartFile(
                "arquivo", "pedido.json",
                MediaType.APPLICATION_JSON_VALUE, jsonContent.getBytes());

        when(arquivosService.checkTipoDocument(any())).thenReturn(1);
        when(arquivosService.converterParaEntidade(any())).thenReturn(Collections.singletonList(new PedidoDTO()));
        when(service.cadastrarPedido(any())).thenReturn(response);

        mockMvc.perform(fileUpload("/api/pedidos/registrar")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", CoreMatchers.is(200)))
                .andExpect(jsonPath("$.success", CoreMatchers.is(true)))
                .andExpect(jsonPath("$.message", CoreMatchers.is(Mensagens.ORDER_SUCCESS_SAVE.value())))
                .andExpect(jsonPath("$.data.size()", CoreMatchers.is(Collections.singletonList(new PedidoDTO()).size())));
    }

    @Test
    void PedidosController_GetAllPedidos_ReturnResponseDto() throws Exception {

        PedidosRespose pedRespIn = PedidosRespose.builder().pageSize(10).last(true).pageNo(1)
                .content(Collections.singletonList(pedido)).build();

        ServiceResponse response = ServiceResponse.builder().dados(pedRespIn)
                .status(true).mensagem(Mensagens.ORDER_SUCCESS_LIST.value()).build();

        when(service.consultarPedido(1, 10, 123L, LocalDate.now(), 1L,
                "produto 1", BigDecimal.valueOf(10.0), 10)).thenReturn(response);

        ResultActions result = mockMvc.perform(get("/api/pedidos")
                .contentType(MediaType.APPLICATION_JSON)
                .param("page", "1")
                .param("size", "10")
                .param("pedido", "123")
                .param("data", LocalDate.now().toString())
                .param("cliente", "1")
                .param("produto", "produto 1")
                .param("preco", "10.0")
                .param("qtde", "10"));

        PedidosRespose pedRespOut = (PedidosRespose) response.getDados();

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.code", CoreMatchers.is(200)))
                .andExpect(jsonPath("$.success", CoreMatchers.is(true)))
                .andExpect(jsonPath("$.message", CoreMatchers.is(Mensagens.ORDER_SUCCESS_LIST.value())))
                .andExpect(jsonPath("$.data.content.size()", CoreMatchers.is(pedRespOut.getContent().size())));
    }

    @Test
    void PedidosController_PedidosDetail_ReturnClientesDto() throws Exception{
        ServiceResponse response = ServiceResponse.builder().status(true)
                .mensagem(Mensagens.ORDER_SUCCESS_FOUND.value()).dados(Collections.singletonList(pedido)).build();
        when(service.consultarPedidoPorId(123L)).thenReturn(response);

        ResultActions result = mockMvc.perform(get("/api/pedidos/123")
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.code", CoreMatchers.is(200)))
                .andExpect(jsonPath("$.success", CoreMatchers.is(true)))
                .andExpect(jsonPath("$.message", CoreMatchers.is(Mensagens.ORDER_SUCCESS_FOUND.value())))
                .andExpect(jsonPath("$.data[0].controle", CoreMatchers.is(pedido.getControle().intValue())))
                .andExpect(jsonPath("$.data[0].cliente", CoreMatchers.is(pedido.getClientId().intValue())))
                .andExpect(jsonPath("$.data[0].produto", CoreMatchers.is(pedido.getProduto())))
                .andExpect(jsonPath("$.data[0].quantidade", CoreMatchers.is(pedido.getQuantidade())))
                .andExpect(jsonPath("$.data[0].valor", CoreMatchers.is(pedido.getValor().doubleValue())))
                .andExpect(jsonPath("$.data[0].registro", CoreMatchers.is(pedido.getDtRegistro())));
    }

    @Test
    void PedidosController_UpdatePedidos_ReturnClientesDto() throws Exception {
        ServiceResponse response = ServiceResponse.builder().status(true)
                .mensagem(Mensagens.ORDER_SUCCESS_UPDT.value()).dados(Collections.singletonList(pedido)).build();

        when(service.atualizarPedido(pedido, 123L)).thenReturn(response);

        ObjectMapper objectMapper = new ObjectMapper();
        String pedidoJson = objectMapper.writeValueAsString(pedido);

        ResultActions result = mockMvc.perform(put("/api/pedidos/123/atualizar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(pedidoJson));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.code", CoreMatchers.is(200)))
                .andExpect(jsonPath("$.success", CoreMatchers.is(true)))
                .andExpect(jsonPath("$.message", CoreMatchers.is(Mensagens.ORDER_SUCCESS_UPDT.value())))
                .andExpect(jsonPath("$.data[0].controle", CoreMatchers.is(pedido.getControle().intValue())))
                .andExpect(jsonPath("$.data[0].cliente", CoreMatchers.is(pedido.getClientId().intValue())))
                .andExpect(jsonPath("$.data[0].produto", CoreMatchers.is(pedido.getProduto())))
                .andExpect(jsonPath("$.data[0].quantidade", CoreMatchers.is(pedido.getQuantidade())))
                .andExpect(jsonPath("$.data[0].valor", CoreMatchers.is(pedido.getValor().doubleValue())))
                .andExpect(jsonPath("$.data[0].registro", CoreMatchers.is(pedido.getDtRegistro())));
    }

    @Test
    void PedidosController_DeletePedido_ReturnString() throws Exception {
        ServiceResponse response = ServiceResponse.builder().status(true)
                .mensagem(Mensagens.ORDER_SUCCESS_DEL.value()).dados(null).build();
        when(service.deletarPedido(123L)).thenReturn(response);

        ResultActions result = mockMvc.perform(delete("/api/pedidos/123/deletar")
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.code", CoreMatchers.is(200)))
                .andExpect(jsonPath("$.success", CoreMatchers.is(true)))
                .andExpect(jsonPath("$.message", CoreMatchers.is(Mensagens.ORDER_SUCCESS_DEL.value())));
    }
}
