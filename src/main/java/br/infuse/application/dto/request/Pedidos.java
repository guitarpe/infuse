package br.infuse.application.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Pedidos {
    @JsonProperty("pedidos")
    public List<Pedido> pedidos;
}
