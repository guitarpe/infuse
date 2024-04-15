package br.infuse.application.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ErrorResponse {
	private long code;
	private Date timestamp;
    private String message;
    private String details;

    public ErrorResponse(long code, Date timestamp, String message, String details) {
         super();
         this.code = code;
         this.timestamp = timestamp;
         this.message = message;
         this.details = details;
    }
}
