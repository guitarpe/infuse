package br.infuse.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SuccessResponse {
    private long code;
    private boolean sucesso;
	private LocalDateTime timestamp;
    private String message;
    private Object data;
}
