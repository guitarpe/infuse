package br.infuse.application.controller;

import br.infuse.application.dto.request.Order;
import br.infuse.application.dto.response.ServiceResponse;
import br.infuse.application.dto.response.SuccessResponse;
import br.infuse.application.enuns.MessageSystem;
import br.infuse.application.service.IFilesService;
import br.infuse.application.service.IOrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/api/orders")
public class OrdersController {

    @Autowired
    private IOrdersService orderService;

    @Autowired
    private IFilesService filesService;

    @PostMapping(value="/receive",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SuccessResponse> receive(@RequestParam("file") MultipartFile file) throws Exception{
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();

        if(filesService.checkDocumentType(file) < 1)
            throw new EntityNotFoundException(MessageSystem.ERROR_INVALID_FILE.value());

        List<Order> orders = filesService.fileToEntity(file, methodName);

        if(orders.isEmpty())
            throw new EntityNotFoundException(MessageSystem.ERROR_CONVERT_FILE.value());

        ServiceResponse successDetails = orderService.saveOrder(orders, methodName);

        if (!successDetails.isStatus()) {
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
    @PostMapping(value="/consult",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponse> consult(@Valid @RequestBody Order order) throws Exception{

        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        ServiceResponse successDetails = orderService.getOrder(order, methodName);

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
