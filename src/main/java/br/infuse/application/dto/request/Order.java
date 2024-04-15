package br.infuse.application.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @JsonProperty("client")
    private Long clientId;

    @JsonProperty("control")
    private Long numControl;

    @JsonProperty("product")
    private String nmProduct;

    @JsonProperty("value")
    private double vlProduct;

    @JsonProperty("amount")
    @Builder.Default
    private Integer amountOrder = 1;

    @JsonProperty("register")
    private String dtRegister;
}
