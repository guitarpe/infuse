package br.infuse.application.controller;

import br.infuse.application.dto.request.PedidoDTO;
import br.infuse.application.dto.response.ServiceResponse;
import br.infuse.application.dto.response.SuccessResponse;
import br.infuse.application.enuns.Mensagens;
import br.infuse.application.service.ArquivosService;
import br.infuse.application.service.PedidosService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public class PedidosController {

    @Autowired
    private PedidosService service;

    @Autowired
    private ArquivosService arquivoService;

    @PostMapping(value="/pedidos/registrar",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Pedidos registrados com sucesso"),
            @ApiResponse(code = 500, message = "Erro interno do servidor")
    })
    public ResponseEntity<SuccessResponse> registrar(@RequestParam("arquivo") MultipartFile arquivo) {

        if(arquivoService.checkTipoDocument(arquivo) < 1)
            throw new EntityNotFoundException(Mensagens.FILE_ERROR_INVALID_FILE.value());

        List<PedidoDTO> pedidoDTOS = arquivoService.converterParaEntidade(arquivo);

        if(pedidoDTOS.isEmpty())
            throw new EntityNotFoundException(Mensagens.FILE_ERROR_CONVERT_FILE.value());

        ServiceResponse retorno = service.cadastrarPedido(pedidoDTOS);

        if (!retorno.isStatus()) {
            throw new EntityNotFoundException(retorno.getMensagem());
        }

        return ResponseEntity.ok().body(SuccessResponse.builder()
                .code(200)
                .success(true)
                .timestamp(LocalDateTime.now())
                .message(retorno.getMensagem())
                .data(retorno.getDados()).build());
    }

    @GetMapping(value="/pedidos",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Pedido recuperado com sucesso"),
            @ApiResponse(code = 404, message = "Pedido não encontrado"),
            @ApiResponse(code = 500, message = "Erro interno do servidor")
    })
    public ResponseEntity<SuccessResponse> pedidos(@RequestParam(value="page", defaultValue="0") int page,
                                                   @RequestParam(value="size", defaultValue="10") int size,
                                                   @RequestParam(value = "pedido") long pedido,
                                                   @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
                                                   @RequestParam(value = "cliente", required = false) Long cliente,
                                                   @RequestParam(value = "produto", required = false) String produto,
                                                   @RequestParam(value = "preco", required = false) BigDecimal preco,
                                                   @RequestParam(value = "qtde", required = false) Integer qtde) {

        ServiceResponse retorno = service.consultarPedido(page, size, pedido, data, cliente, produto, preco, qtde);

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

    @GetMapping(value="/pedidos/{controle}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Pedido recuperado com sucesso"),
            @ApiResponse(code = 404, message = "Pedido não encontrado"),
            @ApiResponse(code = 500, message = "Erro interno do servidor")
    })
    public ResponseEntity<SuccessResponse> pedido(@PathVariable long controle) {

        ServiceResponse retorno = service.consultarPedidoPorId(controle);

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

    @PutMapping(value="/pedidos/{controle}/atualizar",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Pedido atualizado com sucesso"),
            @ApiResponse(code = 500, message = "Erro interno do servidor")
    })
    public ResponseEntity<SuccessResponse> atualizar(@RequestBody PedidoDTO pedido,
                                                     @PathVariable("controle") long controle) {

        ServiceResponse retorno = service.atualizarPedido(pedido, controle);

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

    @DeleteMapping(value="/pedidos/{controle}/deletar",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Pedido deletado com sucesso"),
            @ApiResponse(code = 500, message = "Erro interno do servidor")
    })
    public ResponseEntity<SuccessResponse> deletar(@PathVariable("controle") long controle) {

        ServiceResponse retorno = service.deletarPedido(controle);

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
