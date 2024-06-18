package br.infuse.application.service;

import br.infuse.application.dto.request.PedidoDTO;
import br.infuse.application.dto.response.PagesRespose;
import br.infuse.application.dto.response.ServiceResponse;
import br.infuse.application.enuns.Mensagens;
import br.infuse.application.exception.NotFoundException;
import br.infuse.application.model.Pedidos;
import br.infuse.application.repository.IPedidosRepository;
import br.infuse.application.utils.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PedidosService {

    private final IPedidosRepository repository;

    public ServiceResponse cadastrarPedido(List<PedidoDTO> lista) {
        String mensagem = Mensagens.ORDER_SUCCESS_SAVE.value();
        List<PedidoDTO> pedidos = new ArrayList<>();
        boolean status = true;

        try {
            lista.forEach(objeto -> {

                int count = repository.countByControle(objeto.getControle());

                if(count < 1){
                    LocalDate dataHoje;
                    BigDecimal percDiscount = BigDecimal.ZERO;
                    BigDecimal vOrder;
                    BigDecimal vTotal = objeto.getValor().multiply(BigDecimal.valueOf(objeto.getQuantidade()));

                    if (Utils.verificarVazioOuNulo(objeto.getDtRegistro())) {
                        dataHoje = Utils.stringToLocalDate(Utils.pegarDataAtual());
                    } else {
                        dataHoje = Utils.stringToLocalDate(objeto.getDtRegistro());
                    }

                    Pedidos pedido = new Pedidos();
                    pedido.setControle(objeto.getControle());
                    pedido.setDtRegistro(dataHoje);

                    vOrder = getDiscountOrder(objeto, percDiscount, vTotal, pedido);

                    pedido.setVlPedido(vOrder);

                    repository.save(pedido);

                    pedidos.add(objeto);
                }
            });
        } catch (Exception ex) {
            status = false;
            mensagem = ex.getMessage();
        }

        return ServiceResponse.builder()
                .status(status)
                .mensagem(mensagem)
                .dados(pedidos).build();
    }

    public ServiceResponse atualizarPedido(PedidoDTO objeto, Long controle) throws NotFoundException {
        String mensagem = Mensagens.ORDER_SUCCESS_UPDT.value();
        List<PedidoDTO> pedidos = new ArrayList<>();

        try {
            LocalDate dataHoje;
            BigDecimal percDiscount = BigDecimal.ZERO;
            BigDecimal vOrder;
            BigDecimal vTotal = objeto.getValor().multiply(BigDecimal.valueOf(objeto.getQuantidade()));

            dataHoje = Utils.stringToLocalDate(Utils.pegarDataAtual());

            Pedidos pedido = repository.findByControle(controle)
                    .orElseThrow(() -> new NotFoundException(Mensagens.NO_RESULTS.value()));

            vOrder = getDiscountOrder(objeto, percDiscount, vTotal, pedido);
            pedido.setDtUpdate(dataHoje);
            pedido.setVlPedido(vOrder);

            Pedidos pedidoUpdt = repository.save(pedido);

            pedidos.add(mapToDto(pedidoUpdt));

        } catch (Exception ex) {
            throw ex;
        }

        return ServiceResponse.builder()
                .status(true)
                .mensagem(mensagem)
                .dados(pedidos).build();
    }

    public ServiceResponse consultarPedidoPorId(Long controle) throws NotFoundException {
        List<PedidoDTO> pedidos = new ArrayList<>();
        String mensagem = Mensagens.ORDER_SUCCESS_FOUND.value();

        try {
            Pedidos pedido = repository.findByControle(controle)
                    .orElseThrow(() -> new NotFoundException(Mensagens.NO_RESULTS.value()));

            pedidos.add(mapToDto(pedido));

        } catch (Exception ex) {
            throw ex;
        }

        return ServiceResponse.builder()
                .status(true)
                .mensagem(mensagem)
                .dados(pedidos).build();
    }

    public ServiceResponse consultarPedido(int page, int size, long pedido, LocalDate registro, Long cliente,
                                           String produto, BigDecimal valor, Integer qtde) throws NotFoundException {
        List<PedidoDTO> content;
        String mensagem = Mensagens.ORDER_SUCCESS_LIST.value();

        PagesRespose pedidosResponse = new PagesRespose();

        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Pedidos> pedidos = repository.searchOrders(pedido, registro, qtde, valor, produto, cliente, pageable);
            List<Pedidos> listOfPedidos = pedidos.getContent();
            content = listOfPedidos.stream().map(this::mapToDto).collect(Collectors.toList());

            if (content.isEmpty())
                throw new NotFoundException(Mensagens.ORDER_ERROR_LIST.value());

            pedidosResponse.setContent(content);
            pedidosResponse.setPageNo(pedidos.getNumber());
            pedidosResponse.setPageSize(pedidos.getSize());
            pedidosResponse.setTotalElements(pedidos.getTotalElements());
            pedidosResponse.setTotalPages(pedidos.getTotalPages());
            pedidosResponse.setLast(pedidos.isLast());

        } catch (Exception ex) {
            throw ex;
        }

        return ServiceResponse.builder()
                .status(true)
                .mensagem(mensagem)
                .dados(pedidosResponse).build();
    }

    public ServiceResponse deletarPedido(Long controle) throws NotFoundException {
        String mensagem = Mensagens.ORDER_SUCCESS_DEL.value();

        try {
            Pedidos consulta = repository.findByControle(controle)
                    .orElseThrow(() -> new NotFoundException(Mensagens.NO_RESULTS.value()));

            repository.delete(consulta);

        } catch (Exception ex) {
            throw ex;
        }

        return ServiceResponse.builder()
                .status(true)
                .mensagem(mensagem)
                .dados(null).build();
    }

    private BigDecimal getDiscountOrder(PedidoDTO objeto, BigDecimal percDiscount, BigDecimal vTotal, Pedidos pedido) {
        BigDecimal vDiscount;
        BigDecimal vOrder;
        if (objeto.getQuantidade() > 5 && objeto.getQuantidade() < 10) {
            percDiscount = new BigDecimal("0.05");
        } else if (objeto.getQuantidade() >= 10) {
            percDiscount = new BigDecimal("0.10");
        }

        vDiscount = vTotal.multiply(percDiscount);
        vOrder = vTotal.subtract(vDiscount);

        pedido.setCliente(objeto.getClientId());
        pedido.setNomeProduto(objeto.getProduto());
        pedido.setVlProduto(objeto.getValor());
        pedido.setQuantidade(objeto.getQuantidade());
        pedido.setPercDesconto(percDiscount);
        pedido.setVlDesconto(vDiscount);

        return vOrder;
    }

    private PedidoDTO mapToDto(Pedidos pedido) {
        return PedidoDTO.builder()
                .controle(pedido.getControle())
                .clientId(pedido.getCliente())
                .produto(pedido.getNomeProduto())
                .quantidade(pedido.getQuantidade())
                .valor(pedido.getVlProduto())
                .dtRegistro(Utils.dateToString(pedido.getDtRegistro())).build();
    }
}
