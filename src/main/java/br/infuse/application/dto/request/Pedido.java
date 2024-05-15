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
public class Pedido {

    @JsonProperty("cliente")
    private Long clientId;

    @JsonProperty("controle")
    private Long controle;

    @JsonProperty("produto")
    private String produto;

    @JsonProperty("valor")
    private double valor;

    @JsonProperty("quantidade")
    @Builder.Default
    private Integer quantidade = 1;

    @JsonProperty("registro")
    private String dtRegistro;
}
