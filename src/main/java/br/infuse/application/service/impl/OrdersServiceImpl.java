package br.infuse.application.service.impl;

import br.infuse.application.dto.request.OrderDTO;
import br.infuse.application.dto.response.ServiceResponse;
import br.infuse.application.enuns.MessageSystem;
import br.infuse.application.model.Orders;
import br.infuse.application.repository.IClientsRepository;
import br.infuse.application.repository.IOrdersRepository;
import br.infuse.application.service.IOrdersService;
import br.infuse.application.service.ILogsService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrdersServiceImpl implements IOrdersService {

    private final IOrdersRepository ordersRepository;
    private final IClientsRepository clientsRepository;
    private final ILogsService logsService;

    @Override
    public ServiceResponse saveOrder(List<OrderDTO> orders, String method) throws Exception {
        try {
            Gson gson = new Gson();
            String json;
            Map<String, String> orderData = new HashMap<>();
            List<String> clients = new ArrayList<>();
            List<String> controls = new ArrayList<>();
            List<OrderDTO> listOrders = new ArrayList<>();

            orders.forEach(order -> {
                double totalValue;
                double perDiscount = 0;
                double vlDiscount;
                double vlOrder;

                if (Objects.isNull(clientsRepository.findByClientId(order.getClientId())))
                    clients.add(order.getClientId().toString());

                if (!Objects.isNull(ordersRepository.findByNumControl(order.getNumControl())))
                    controls.add(order.getNumControl().toString());

                if (clients.size() == 0 || controls.size() == 0) {

                    totalValue = order.getAmountOrder() * order.getVlProduct();

                    if (order.getAmountOrder() > 5) {
                        perDiscount = 0.05;
                    } else if (order.getAmountOrder() > 10) {
                        perDiscount = 0.10;
                    }

                    vlDiscount = totalValue * perDiscount;
                    vlOrder = totalValue - vlDiscount;

                    ordersRepository.save(Orders.builder()
                            .clientId(order.getClientId())
                            .numControl(order.getNumControl())
                            .nmProduct(order.getNmProduct())
                            .vlProduct(order.getVlProduct())
                            .amountOrder(order.getAmountOrder())
                            .percentDiscount(perDiscount)
                            .vlDiscount(vlDiscount)
                            .vlOrder(vlOrder)
                            .dtRegister(order.getDtRegister())
                            .build());

                    listOrders.add(order);
                }
            });

            json = gson.toJson(listOrders);
            orderData.put("Pedidos registrados", json);

            if (!clients.isEmpty()) {
                json = gson.toJson(clients);
                orderData.put("Clientes não cadastrados", json);
            }

            if (!controls.isEmpty()) {
                json = gson.toJson(clients);
                orderData.put("Pedidos já cadastrados", json);
            }

            return ServiceResponse.builder()
                    .status(true)
                    .message(MessageSystem.SUCCESS_SAVE_ORDER.value())
                    .data(orderData).build();

        }catch (Exception ex){
            String erroMsg = MessageSystem.ERROR_SAVE_ORDER.value()+ex.getMessage();
            logsService.addLogExec(erroMsg, method);
            throw new Exception(erroMsg);
        }
    }

    @Override
    public ServiceResponse getOrder(OrderDTO order, String method) throws Exception {
        try{
            List<Orders> orders = ordersRepository.searchOrders(order.getNumControl(), order.getDtRegister(),
                    order.getAmountOrder(), order.getVlProduct(), order.getNmProduct(), order.getClientId());

            if(Objects.isNull(orders))
                throw new Exception(MessageSystem.ERROR_LIST_NOT_FOUND.value());

            return ServiceResponse.builder()
                    .status(true)
                    .message(MessageSystem.SUCCESS_LIST_ORDERS.value())
                    .data(orders).build();
        }catch (Exception ex){
            String erroMsg = MessageSystem.ERROR_LIST_ORDERS.value()+ex.getMessage();
            logsService.addLogExec(erroMsg, method);
            throw new Exception(erroMsg);
        }
    }
}
