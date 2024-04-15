package br.infuse.application.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Client {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String nmClient;

    @JsonProperty("register")
    private String dtRegister;
}
