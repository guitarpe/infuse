package br.infuse.application.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderRoot {
    @JsonProperty("orders")
    public OrdersList ordersList;
}
