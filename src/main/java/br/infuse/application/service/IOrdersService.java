package br.infuse.application.service;

import br.infuse.application.dto.request.OrderDTO;
import br.infuse.application.dto.response.ServiceResponse;

import java.util.List;

public interface IOrdersService {
    ServiceResponse saveOrder(List<OrderDTO> orders, String methodName) throws Exception;
    ServiceResponse getOrder(OrderDTO order, String methodName) throws Exception;
}
