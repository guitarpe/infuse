package br.infuse.application.controller;

import br.infuse.application.dto.request.ClienteDTO;
import br.infuse.application.dto.response.ServiceResponse;
import br.infuse.application.dto.response.SuccessResponse;
import br.infuse.application.exception.NotFoundException;
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
@RequestMapping("/api")
public class ClientesController {

    @Autowired
    private ClientesService service;

    @GetMapping(value="/clientes",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Cliente recuperado com sucesso"),
            @ApiResponse(code = 404, message = "Cliente não encontrado"),
            @ApiResponse(code = 500, message = "Erro interno do servidor")
    })
    public ResponseEntity<SuccessResponse> clientes(
            @RequestParam(value="page", defaultValue="0", required=false) int page,
            @RequestParam(value="size", defaultValue="10", required=false) int size) throws NotFoundException {

        ServiceResponse retorno = service.consultarClientes(page, size);

        return ResponseEntity.ok().body(SuccessResponse.builder()
                .code(200)
                .success(true)
                .timestamp(LocalDateTime.now())
                .message(retorno.getMensagem())
                .data(retorno.getDados()).build());
    }

    @GetMapping(value="/clientes/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Cliente recuperado com sucesso"),
            @ApiResponse(code = 404, message = "Cliente não encontrado"),
            @ApiResponse(code = 500, message = "Erro interno do servidor")
    })
    public ResponseEntity<SuccessResponse> cliente(@PathVariable long id) throws NotFoundException {
        ServiceResponse retorno = service.consultarClientePorId(id);

        return ResponseEntity.ok().body(SuccessResponse.builder()
                .code(200)
                .success(true)
                .timestamp(LocalDateTime.now())
                .message(retorno.getMensagem())
                .data(retorno.getDados()).build());
    }

    @PostMapping(value="/clientes/registrar",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Cliente registrado com sucesso"),
            @ApiResponse(code = 500, message = "Erro interno do servidor")
    })
    public ResponseEntity<SuccessResponse> registrar(@RequestBody ClienteDTO cliente) {
        ServiceResponse retorno = service.cadastrarClientes(cliente);

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

    @PutMapping("/clientes/{id}/atualizar")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Cliente atualizado com sucesso"),
            @ApiResponse(code = 404, message = "Cliente não encontrado"),
            @ApiResponse(code = 500, message = "Erro interno do servidor")
    })
    public ResponseEntity<SuccessResponse> atualizar(@RequestBody ClienteDTO cliente,
                                                     @PathVariable("id") long id) throws NotFoundException {
        ServiceResponse retorno = service.atualizarClientes(cliente, id);

        return ResponseEntity.ok().body(SuccessResponse.builder()
                .code(200)
                .success(true)
                .timestamp(LocalDateTime.now())
                .message(retorno.getMensagem())
                .data(retorno.getDados()).build());
    }

    @DeleteMapping("/clientes/{id}/deletar")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Cliente deletado com sucesso"),
            @ApiResponse(code = 404, message = "Cliente não encontrado"),
            @ApiResponse(code = 500, message = "Erro interno do servidor")
    })
    public ResponseEntity<SuccessResponse> deletar(@PathVariable("id") long id) throws NotFoundException {

        ServiceResponse retorno = service.deletarClientes(id);

        return ResponseEntity.ok().body(SuccessResponse.builder()
                .code(200)
                .success(true)
                .timestamp(LocalDateTime.now())
                .message(retorno.getMensagem())
                .data(retorno.getDados()).build());
    }
}
