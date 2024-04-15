package br.infuse.application.controller;

import br.infuse.application.dto.request.Client;
import br.infuse.application.dto.request.ClientsList;
import br.infuse.application.dto.response.ServiceResponse;
import br.infuse.application.dto.response.SuccessResponse;
import br.infuse.application.service.IClientsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/api/clients")
public class ClientsController {

    @Autowired
    private IClientsService clientsService;

    @Validated
    @PostMapping(value="/consult",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponse> consult(@Valid @RequestBody Client client) throws Exception{
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        ServiceResponse successDetails = clientsService.searchClient(client, methodName);

        if(!successDetails.isStatus()) {
            throw new EntityNotFoundException(successDetails.getMessage());
        }

        return ResponseEntity.ok().body(SuccessResponse.builder()
                .code(200)
                .success(true)
                .timestamp(LocalDateTime.now())
                .message(successDetails.getMessage())
                .data(successDetails.getData()).build());
    }

    @Validated
    @PostMapping(value="/register",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponse> register(@Valid @RequestBody ClientsList clients) throws Exception{
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        ServiceResponse successDetails = clientsService.saveClient(clients, methodName);

        if(!successDetails.isStatus()) {
            throw new EntityNotFoundException(successDetails.getMessage());
        }

        return ResponseEntity.ok().body(SuccessResponse.builder()
                .code(200)
                .success(true)
                .timestamp(LocalDateTime.now())
                .message(successDetails.getMessage())
                .data(successDetails.getData()).build());
    }
}
