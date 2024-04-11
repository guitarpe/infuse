package br.infuse.application.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class OrderDTO {

    @JsonProperty("cliente")
    private Long clientId;

    @JsonProperty("controle")
    private Long numControl;

    @JsonProperty("produto")
    private String nmProduct;

    @JsonProperty("valor")
    private Long vlProduct;

    @JsonProperty("quantidade")
    private Integer amountOrder;

    @JsonProperty("cadastro")
    private LocalDateTime dtRegister;
}
