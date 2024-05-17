package br.infuse.application.service;

import br.infuse.application.dto.request.Pedido;
import br.infuse.application.dto.response.DataResponse;
import br.infuse.application.dto.response.ServiceResponse;
import br.infuse.application.enuns.Mensagens;
import br.infuse.application.model.Clientes;
import br.infuse.application.model.Pedidos;
import br.infuse.application.repository.IClientesRepository;
import br.infuse.application.repository.IPedidosProcedureRepository;
import br.infuse.application.repository.IPedidosRepository;
import br.infuse.application.utils.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PedidosService{

    private final IPedidosRepository pedidos;
    private final IPedidosProcedureRepository procedure;
    private final IClientesRepository clientes;

    public ServiceResponse cadastrarPedido(List<Pedido> lista) {
        String mensagem = Mensagens.SUCCESS_SAVE_ORDER.value();
        DataResponse retData = new DataResponse();
        boolean status = true;

        try {
            lista.forEach(order -> {
                LocalDateTime dataHoje = LocalDateTime.now();

                Optional<Clientes> cliente = clientes.findByClientId(order.getClientId());
                Optional<Pedidos> pedido = pedidos.findByNumControl(order.getControle());

                if (!cliente.isPresent())
                    retData.getClients().add(order.getClientId().toString());

                if (pedido.isPresent())
                    retData.getControls().add(order.getControle().toString());

                if (cliente.isPresent() && !pedido.isPresent()) {
                    if(!Utils.verificarVazioOuNulo(order.getDtRegistro())) {
                        order.setDtRegistro(Utils.pegarDataAtual());
                    }else{
                        dataHoje = Utils.converterDataString(order.getDtRegistro());
                    }

                    procedure.procRegisterOrders(order.getClientId(), order.getControle(),
                            order.getProduto(), order.getValor(), order.getQuantidade(), dataHoje);

                    retData.getListPedidos().add(order);
                }
            });

            if(retData.getListPedidos().isEmpty()){
                mensagem = Mensagens.NOT_SAVE_ORDERS.value();
            }
        }catch (Exception ex){
            status = false;
            mensagem = Mensagens.ERROR_SAVE_ORDER.value() + ex.getMessage();
        }

        return ServiceResponse.builder()
                .status(status)
                .mensagem(mensagem)
                .dados(retData).build();
    }

    public ServiceResponse consultarPedido(Long pedido, LocalDate registro, Long cliente,
                                           String produto, Double valor, Integer qtde) {
        List<Pedidos> lista = new ArrayList<>();
        String mensagem = Mensagens.SUCCESS_LIST_ORDERS.value();
        boolean status = true;

        try{
            lista = pedidos.searchOrders(pedido, registro, qtde, valor, produto, cliente);

            if(lista.isEmpty())
                throw new EntityNotFoundException(Mensagens.ERROR_LIST_NOT_FOUND.value());

        }catch (Exception ex){
            status = false;
            mensagem = Mensagens.ERROR_LIST_ORDERS.value()+ex.getMessage();
        }

        return ServiceResponse.builder()
                .status(status)
                .mensagem(mensagem)
                .dados(lista).build();
    }
}
