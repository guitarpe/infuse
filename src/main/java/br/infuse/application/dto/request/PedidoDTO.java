package br.infuse.application.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoDTO {

    @JsonProperty("cliente")
    private Long clientId;

    @JsonProperty("controle")
    private Long controle;

    @JsonProperty("produto")
    private String produto;

    @JsonProperty("valor")
    private BigDecimal valor;

    @JsonProperty("quantidade")
    @Builder.Default
    private Integer quantidade = 1;

    @JsonProperty("registro")
    private String dtRegistro;
}
