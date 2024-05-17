package br.infuse.application.controller;

import br.infuse.application.dto.request.Pedido;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/pedidos")
public class PedidosController {

    @Autowired
    private PedidosService service;

    @Autowired
    private ArquivosService arquivoService;

    @PostMapping(value="/receber",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Pedidos registrados com sucesso"),
            @ApiResponse(code = 500, message = "Erro interno do servidor")
    })
    public ResponseEntity<SuccessResponse> receber(@RequestParam("arquivo") MultipartFile arquivo) {

        if(arquivoService.checkTipoDocument(arquivo) < 1)
            throw new EntityNotFoundException(Mensagens.ERROR_INVALID_FILE.value());

        List<Pedido> pedidos = arquivoService.arquivoToEntidade(arquivo);

        if(pedidos.isEmpty())
            throw new EntityNotFoundException(Mensagens.ERROR_CONVERT_FILE.value());

        ServiceResponse retorno = service.cadastrarPedido(pedidos);

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

    @GetMapping(value="/consultar",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Pedido recuperado com sucesso"),
            @ApiResponse(code = 404, message = "Pedido não encontrado"),
            @ApiResponse(code = 500, message = "Erro interno do servidor")
    })
    public ResponseEntity<SuccessResponse> consultar(@RequestParam(value = "pedido", required = false) Long pedido,
                                                     @RequestParam(value = "data", required = false)
                                                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate registro,
                                                     @RequestParam(value = "cliente", required = false) Long cliente,
                                                     @RequestParam(value = "produto", required = false) String produto,
                                                     @RequestParam(value = "valor", required = false) Double valor,
                                                     @RequestParam(value = "qtde", required = false) Integer qtde) {

        ServiceResponse retorno = service.consultarPedido(pedido, registro, cliente, produto, valor, qtde);

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
