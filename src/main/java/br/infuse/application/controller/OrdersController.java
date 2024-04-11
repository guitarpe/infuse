package br.infuse.application.controller;

import br.infuse.application.dto.request.ClientDTO;
import br.infuse.application.dto.request.OrderDTO;
import br.infuse.application.dto.response.ServiceResponse;
import br.infuse.application.dto.response.SuccessResponse;
import br.infuse.application.service.IClientsService;
import br.infuse.application.service.IFilesService;
import br.infuse.application.service.IOrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/rest/api/orders")
public class OrdersController {

    @Autowired
    private IOrdersService orderService;

    @Autowired
    private IClientsService clientsService;

    @Autowired
    private IFilesService filesService;

    @Validated
    @PostMapping(value="/receive",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SuccessResponse> receive(@Valid @RequestPart(value = "file") MultipartFile file,
                                                   @RequestHeader HttpHeaders headers) throws Exception{

        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        List<OrderDTO> orders = filesService.fileToEntity(file, methodName);

        ServiceResponse successDetails = orderService.saveOrder(orders, methodName);

        if(!successDetails.isStatus()) {
            throw new EntityNotFoundException(successDetails.getMessage());
        }

        return ResponseEntity.ok().body(SuccessResponse.builder()
                        .code(200)
                        .sucesso(true)
                        .timestamp(LocalDateTime.now())
                        .message(successDetails.getMessage())
                        .data(successDetails.getData()).build());
    }

    @Validated
    @PostMapping(value="/consult",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponse> consult(@Valid @RequestBody OrderDTO order,
                                                     @RequestHeader HttpHeaders headers) throws Exception{

        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        ServiceResponse successDetails = orderService.getOrder(order, methodName);

        if(!successDetails.isStatus()) {
            throw new EntityNotFoundException(successDetails.getMessage());
        }

        return ResponseEntity.ok().body(SuccessResponse.builder()
                        .code(200)
                        .sucesso(true)
                        .timestamp(LocalDateTime.now())
                        .message(successDetails.getMessage())
                        .data(successDetails.getData()).build());
    }

    @Validated
    @PostMapping(value="/get-clients",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponse> allClients(@Valid @RequestBody ClientDTO client,
                                                     @RequestHeader HttpHeaders headers) throws Exception{

        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        ServiceResponse successDetails = clientsService.searchClient(client, methodName);

        if(!successDetails.isStatus()) {
            throw new EntityNotFoundException(successDetails.getMessage());
        }

        return ResponseEntity.ok().body(SuccessResponse.builder()
                .code(200)
                .sucesso(true)
                .timestamp(LocalDateTime.now())
                .message(successDetails.getMessage())
                .data(successDetails.getData()).build());
    }

    @Validated
    @PostMapping(value="/save-client",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponse> saveClient(@Valid @RequestBody ClientDTO client,
                                                     @RequestHeader HttpHeaders headers) throws Exception{

        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        ServiceResponse successDetails = clientsService.saveClient(client, methodName);

        if(!successDetails.isStatus()) {
            throw new EntityNotFoundException(successDetails.getMessage());
        }

        return ResponseEntity.ok().body(SuccessResponse.builder()
                .code(200)
                .sucesso(true)
                .timestamp(LocalDateTime.now())
                .message(successDetails.getMessage())
                .data(successDetails.getData()).build());
    }
}
