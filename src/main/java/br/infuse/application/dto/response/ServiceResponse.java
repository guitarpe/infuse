package br.infuse.application.dto.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ServiceResponse {
    private boolean status;
    private String message;
    private Object data;
}
