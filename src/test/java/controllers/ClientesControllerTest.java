package controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.infuse.application.controller.ClientesController;
import br.infuse.application.dto.request.ClienteDTO;
import br.infuse.application.dto.response.ClientesResponse;
import br.infuse.application.dto.response.ServiceResponse;
import br.infuse.application.enuns.Mensagens;
import br.infuse.application.service.ClientesService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

@ExtendWith(MockitoExtension.class)
class ClientesControllerTest {

    @InjectMocks
    private ClientesController controller;

    @Mock
    private ClientesService service;

    @Autowired
    private MockMvc mockMvc;
    private ClienteDTO cliente;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        cliente = new ClienteDTO();
        cliente.setId(1L);
        cliente.setNome("Cliente 1");
        cliente.setEmail("cliente1@email.com");
        cliente.setTelefone("(44) 12345-6789");
        cliente.setDtRegistro("2024-06-14");
    }

    @Test
    void ClientesController_GetAllClientes_ReturnResponseDto() throws Exception {
        ClientesResponse cliRespIn = ClientesResponse.builder().pageSize(10).last(true).pageNo(1)
                .content(Collections.singletonList(cliente)).build();

        ServiceResponse response = ServiceResponse.builder().dados(cliRespIn)
                .status(true).mensagem(Mensagens.CLIENT_SUCCESS_LIST.value()).build();

        when(service.consultarClientes(1, 10)).thenReturn(response);

        ResultActions result = mockMvc.perform(get("/api/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .param("page", "1")
                .param("size", "10"));

        ClientesResponse cliRespOut = (ClientesResponse) response.getDados();

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.code", CoreMatchers.is(200)))
                .andExpect(jsonPath("$.success", CoreMatchers.is(true)))
                .andExpect(jsonPath("$.message", CoreMatchers.is(Mensagens.CLIENT_SUCCESS_LIST.value())))
                .andExpect(jsonPath("$.data.content.size()", CoreMatchers.is(cliRespOut.getContent().size())));
    }

    @Test
    void ClientesController_ClientesDetail_ReturnClientesDto() throws Exception {
        ServiceResponse response = ServiceResponse.builder().status(true)
                .mensagem(Mensagens.CLIENT_SUCCESS_FOUND.value()).dados(Collections.singletonList(cliente)).build();
        when(service.consultarClientePorId(1L)).thenReturn(response);

        ResultActions result = mockMvc.perform(get("/api/clientes/1")
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.code", CoreMatchers.is(200)))
                .andExpect(jsonPath("$.success", CoreMatchers.is(true)))
                .andExpect(jsonPath("$.message", CoreMatchers.is(Mensagens.CLIENT_SUCCESS_FOUND.value())))
                .andExpect(jsonPath("$.data[0].id", CoreMatchers.is(cliente.getId().intValue())))
                .andExpect(jsonPath("$.data[0].nome", CoreMatchers.is(cliente.getNome())))
                .andExpect(jsonPath("$.data[0].email", CoreMatchers.is(cliente.getEmail())))
                .andExpect(jsonPath("$.data[0].telefone", CoreMatchers.is(cliente.getTelefone())))
                .andExpect(jsonPath("$.data[0].registro", CoreMatchers.is(cliente.getDtRegistro())));
    }

    @Test
    void ClientesController_CreateClient_ReturnCreated() throws Exception {
        ServiceResponse response = ServiceResponse.builder().status(true)
                .mensagem(Mensagens.CLIENT_SUCCESS_SAVE.value()).dados(Collections.singletonList(cliente)).build();

        when(service.cadastrarClientes(cliente)).thenReturn(response);

        ObjectMapper objectMapper = new ObjectMapper();
        String clienteJson = objectMapper.writeValueAsString(cliente);

        ResultActions result = mockMvc.perform(post("/api/clientes/registrar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(clienteJson));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.code", CoreMatchers.is(200)))
                .andExpect(jsonPath("$.success", CoreMatchers.is(true)))
                .andExpect(jsonPath("$.message", CoreMatchers.is(Mensagens.CLIENT_SUCCESS_SAVE.value())))
                .andExpect(jsonPath("$.data[0].id", CoreMatchers.is(cliente.getId().intValue())))
                .andExpect(jsonPath("$.data[0].nome", CoreMatchers.is(cliente.getNome())))
                .andExpect(jsonPath("$.data[0].email", CoreMatchers.is(cliente.getEmail())))
                .andExpect(jsonPath("$.data[0].telefone", CoreMatchers.is(cliente.getTelefone())))
                .andExpect(jsonPath("$.data[0].registro", CoreMatchers.is(cliente.getDtRegistro())));
    }

    @Test
    void ClientesController_UpdateClientes_ReturnClientesDto() throws Exception {
        ServiceResponse response = ServiceResponse.builder().status(true)
                .mensagem(Mensagens.CLIENT_SUCCESS_UPDT.value()).dados(Collections.singletonList(cliente)).build();

        when(service.atualizarClientes(cliente, 1L)).thenReturn(response);

        ObjectMapper objectMapper = new ObjectMapper();
        String clienteJson = objectMapper.writeValueAsString(cliente);

        ResultActions result = mockMvc.perform(put("/api/clientes/1/atualizar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(clienteJson));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.code", CoreMatchers.is(200)))
                .andExpect(jsonPath("$.success", CoreMatchers.is(true)))
                .andExpect(jsonPath("$.message", CoreMatchers.is(Mensagens.CLIENT_SUCCESS_UPDT.value())))
                .andExpect(jsonPath("$.data[0].id", CoreMatchers.is(cliente.getId().intValue())))
                .andExpect(jsonPath("$.data[0].nome", CoreMatchers.is(cliente.getNome())))
                .andExpect(jsonPath("$.data[0].email", CoreMatchers.is(cliente.getEmail())))
                .andExpect(jsonPath("$.data[0].telefone", CoreMatchers.is(cliente.getTelefone())))
                .andExpect(jsonPath("$.data[0].registro", CoreMatchers.is(cliente.getDtRegistro())));
    }

    @Test
    void ClientesController_DeleteCliente_ReturnString() throws Exception {
        ServiceResponse response = ServiceResponse.builder().status(true)
                .mensagem(Mensagens.CLIENT_SUCCESS_DEL.value()).dados(null).build();
        when(service.deletarClientes(1L)).thenReturn(response);

        ResultActions result = mockMvc.perform(delete("/api/clientes/1/deletar")
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.code", CoreMatchers.is(200)))
                .andExpect(jsonPath("$.success", CoreMatchers.is(true)))
                .andExpect(jsonPath("$.message", CoreMatchers.is(Mensagens.CLIENT_SUCCESS_DEL.value())));
    }
}
