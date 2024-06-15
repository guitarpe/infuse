package services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import br.infuse.application.dto.request.PedidoDTO;
import br.infuse.application.dto.response.ServiceResponse;
import br.infuse.application.enuns.Mensagens;
import br.infuse.application.model.Clientes;
import br.infuse.application.model.Pedidos;
import br.infuse.application.repository.IClientesRepository;
import br.infuse.application.repository.IPedidosRepository;
import br.infuse.application.service.PedidosService;
import br.infuse.application.utils.Utils;
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
class PedidosServiceTest {

    @Mock
    private IPedidosRepository repository;

    @Mock
    private IClientesRepository clientesRepository;

    @InjectMocks
    private PedidosService service;

    private PedidoDTO pedidoDTO;

    private Pedidos pedido;

    @BeforeEach
    void setUp() {
        pedido = Pedidos.builder()
                .id(12345L)
                .vlProduto(BigDecimal.valueOf(100.00))
                .quantidade(10)
                .nomeProduto("Produto Teste")
                .cliente(1L)
                .dtRegistro(LocalDate.now()).build();

        pedidoDTO = new PedidoDTO();
        pedidoDTO.setClientId(1L);
        pedidoDTO.setControle(12345L);
        pedidoDTO.setProduto("Produto Teste");
        pedidoDTO.setValor(BigDecimal.valueOf(100.00));
        pedidoDTO.setQuantidade(10);
        pedidoDTO.setDtRegistro("2023-06-01");
    }

    @Test
    void PedidosService_CadastrarPedidos_ReturnResponseDto() {
        lenient().when(clientesRepository.findById(any(Long.class))).thenReturn(Optional.of(new Clientes()));
        lenient().when(repository.findById(any(Long.class))).thenReturn(Optional.empty());

        List<PedidoDTO> pedidos = new ArrayList<>();
        pedidos.add(pedidoDTO);

        ServiceResponse response = service.cadastrarPedido(pedidos);

        Assertions.assertThat(response).isNotNull();
    }

    @Test
    void PedidosService_GetAllPedidos_ReturnResponseDto() {
        Page<Pedidos> pedidos = Mockito.mock(Page.class);

        lenient().when(repository.findAll(Mockito.any(Pageable.class))).thenReturn(pedidos);

        ServiceResponse response = service.consultarPedido(1,10, pedidoDTO.getControle(),
                Utils.stringToLocalDate(pedidoDTO.getDtRegistro()), null, null, null, null);

        Assertions.assertThat(response).isNotNull();
    }

    @Test
    void PedidosService_FindById_ReturnResponseDto() {
        long id = 1;
        lenient().when(repository.findById(id)).thenReturn(Optional.ofNullable(pedido));

        ServiceResponse response = service.consultarPedidoPorId(id);

        Assertions.assertThat(response).isNotNull();
    }

    @Test
    void PedidosService_UpdatePedido_ReturnResponseDto() {
        long id = 1;
        lenient().when(repository.findById(id)).thenReturn(Optional.ofNullable(pedido));
        lenient().when(repository.save(pedido)).thenReturn(pedido);

        ServiceResponse response = service.atualizarPedido(pedidoDTO, id);

        Assertions.assertThat(response).isNotNull();
    }

    @Test
    void PedidosService_DeletePedidoById_ReturnVoid() {
        long id = 1;

        when(repository.findByControle(id)).thenReturn(Optional.of(pedido));
        doNothing().when(repository).delete(pedido);

        service.deletarPedido(id);

        verify(repository).findByControle(id);
        verify(repository).delete(pedido);
    }
}

