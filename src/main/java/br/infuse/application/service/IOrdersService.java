package br.infuse.application.service;

import br.infuse.application.dto.request.Order;
import br.infuse.application.dto.response.ServiceResponse;

import java.util.List;

public interface IOrdersService {
    ServiceResponse saveOrder(List<Order> orders, String methodName) throws Exception;
    ServiceResponse getOrder(Order order, String methodName) throws Exception;
}
