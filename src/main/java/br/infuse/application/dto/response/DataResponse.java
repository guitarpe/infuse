package br.infuse.application.dto.response;

import br.infuse.application.dto.request.Client;
import br.infuse.application.dto.request.Order;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class DataResponse {
    @JsonProperty("clientsNotExists")
    List<String> clients = new ArrayList<>();
    @JsonProperty("ordersExists")
    List<String> controls = new ArrayList<>();
    @JsonProperty("ordersAdd")
    List<Order> listOrders = new ArrayList<>();
}
