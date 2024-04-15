package br.infuse.application.service.impl;

import br.infuse.application.dto.request.Order;
import br.infuse.application.dto.response.DataResponse;
import br.infuse.application.dto.response.ServiceResponse;
import br.infuse.application.enuns.MessageSystem;
import br.infuse.application.model.Clients;
import br.infuse.application.model.Orders;
import br.infuse.application.repository.IClientsRepository;
import br.infuse.application.repository.IOrdersProcedureRepository;
import br.infuse.application.repository.IOrdersRepository;
import br.infuse.application.service.IOrdersService;
import br.infuse.application.service.ILogsService;
import br.infuse.application.utils.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrdersServiceImpl implements IOrdersService {

    private final IOrdersRepository ordersRepository;
    private final IOrdersProcedureRepository ordersProcedureRepository;
    private final IClientsRepository clientsRepository;
    private final ILogsService logsService;

    @Override
    public ServiceResponse saveOrder(List<Order> orders, String method) {
        try {
            String retMessage = MessageSystem.SUCCESS_SAVE_ORDER.value();
            DataResponse retData = new DataResponse();

            orders.forEach(order -> {
                LocalDateTime localDateTime = LocalDateTime.now();

                Optional<Clients> client = clientsRepository.findByClientId(order.getClientId());
                Optional<Orders> getorder = ordersRepository.findByNumControl(order.getNumControl());

                if (!client.isPresent())
                    retData.getClients().add(order.getClientId().toString());

                if (getorder.isPresent())
                    retData.getControls().add(order.getNumControl().toString());

                if (client.isPresent() && !getorder.isPresent()) {
                    if(!Utils.verifyEmptyOrNull(order.getDtRegister())) {
                        localDateTime = Utils.convertDtString(order.getDtRegister());
                    }else{
                        order.setDtRegister(Utils.pegarDataAtual());
                    }

                    ordersProcedureRepository.procRegisterOrders(order.getClientId(), order.getNumControl(),
                            order.getNmProduct(), order.getVlProduct(), order.getAmountOrder(), localDateTime);

                    retData.getListOrders().add(order);
                }
            });

            if(retData.getListOrders().isEmpty()){
                retMessage = MessageSystem.NOT_SAVE_ORDERS.value();
            }

            return ServiceResponse.builder()
                    .status(true)
                    .message(retMessage)
                    .data(retData).build();

        }catch (Exception ex){
            String erroMsg = MessageSystem.ERROR_SAVE_ORDER.value() + ex.getMessage();
            logsService.addLogExec(erroMsg, method);
            return ServiceResponse.builder()
                    .status(false)
                    .message(erroMsg)
                    .data(null).build();
        }
    }

    @Override
    public ServiceResponse getOrder(Order order, String method) {
        try{
            List<Orders> orders = ordersRepository.searchOrders(
                    order.getNumControl(), Utils.convertDtString(order.getDtRegister()),order.getAmountOrder(),
                    order.getVlProduct(), order.getNmProduct(), order.getClientId());

            if(orders.isEmpty())
                throw new EntityNotFoundException(MessageSystem.ERROR_LIST_NOT_FOUND.value());

            return ServiceResponse.builder()
                    .status(true)
                    .message(MessageSystem.SUCCESS_LIST_ORDERS.value())
                    .data(orders).build();
        }catch (Exception ex){
            String erroMsg = MessageSystem.ERROR_LIST_ORDERS.value()+ex.getMessage();
            logsService.addLogExec(erroMsg, method);
            return ServiceResponse.builder()
                    .status(false)
                    .message(erroMsg)
                    .data(null).build();
        }
    }
}
