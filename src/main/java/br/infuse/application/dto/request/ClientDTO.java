package br.infuse.application.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Builder
@Data
public class ClientDTO {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("nome")
    private String nmClient;

    @JsonProperty("cadastro")
    private Date dtRegister;
}
