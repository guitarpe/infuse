package br.infuse.application.controller;

import br.infuse.application.dto.request.Clientes;
import br.infuse.application.dto.response.ServiceResponse;
import br.infuse.application.dto.response.SuccessResponse;
import br.infuse.application.enuns.Mensagens;
import br.infuse.application.service.ClientesService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/api/clientes")
public class ClientesController {

    @Autowired
    private ClientesService service;

    @GetMapping(value="/consultar",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Cliente recuperado com sucesso"),
            @ApiResponse(code = 404, message = "Cliente não encontrado"),
            @ApiResponse(code = 500, message = "Erro interno do servidor")
    })
    public ResponseEntity<SuccessResponse> consultar(@RequestParam(value = "nome", required = false) String nome,
                                                     @RequestParam(value = "id", required = false) Long id) {
        if (nome == null && id == null) {
            throw new IllegalArgumentException(Mensagens.ERROR_MSG_NO_PARAMS.value());
        }

        ServiceResponse retorno = service.consultarClientes(nome, id);

        if(!retorno.isStatus()) {
            throw new EntityNotFoundException(retorno.getMensagem());
        }

        return ResponseEntity.ok().body(SuccessResponse.builder()
                .code(200)
                .success(true)
                .timestamp(LocalDateTime.now())
                .message(retorno.getMensagem())
                .data(retorno.getDados()).build());
    }

    @PostMapping(value="/cadastrar",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Cliente registrado com sucesso"),
            @ApiResponse(code = 500, message = "Erro interno do servidor")
    })
    public ResponseEntity<SuccessResponse> cadastrar(@RequestBody Clientes clientes) {
        ServiceResponse retorno = service.cadastrarClientes(clientes);

        if(!retorno.isStatus()) {
            throw new EntityNotFoundException(retorno.getMensagem());
        }

        return ResponseEntity.ok().body(SuccessResponse.builder()
                .code(200)
                .success(true)
                .timestamp(LocalDateTime.now())
                .message(retorno.getMensagem())
                .data(retorno.getDados()).build());
    }
}
