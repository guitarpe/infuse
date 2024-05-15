package br.infuse.application.service;

import br.infuse.application.dto.request.Clientes;
import br.infuse.application.dto.response.DataResponse;
import br.infuse.application.dto.response.ServiceResponse;
import br.infuse.application.enuns.Mensagens;
import br.infuse.application.repository.IClientesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ClientesService {

    private final IClientesRepository clientes;

    public ServiceResponse cadastrarClientes(Clientes lista) {
        String mensagem = Mensagens.SUCCESS_SAVE_CLIENT.value();
        DataResponse retData = new DataResponse();
        boolean status = true;

        try {
            List<Long> naoCadastrados = new ArrayList<>();

            lista.getClientes().forEach(client -> {
                Optional<br.infuse.application.model.Clientes> consulta = clientes.findByNmClient(client.getNome());

                if (!consulta.isPresent()) {
                    clientes.save(br.infuse.application.model.Clientes.builder()
                            .nmClient(client.getNome())
                            .dtRegister(LocalDateTime.now())
                            .build());

                    naoCadastrados.add(client.getId());
                }
            });

            if(naoCadastrados.isEmpty()){
                mensagem = Mensagens.ERROR_NOT_CLIENT_SAVE.value();
            }
        } catch (Exception ex) {
            status = false;
            mensagem = Mensagens.ERROR_SAVE_CLIENT.value() + ex.getMessage();
        }

        return ServiceResponse.builder()
                .status(status)
                .mensagem(mensagem)
                .dados(retData).build();
    }

    public ServiceResponse consultarClientes(String nome, Long id) {
        List<br.infuse.application.model.Clientes> lista = new ArrayList<>();
        String mensagem = Mensagens.SUCCESS_LIST_CLIENTS.value();
        boolean status = true;

        try {
            lista = clientes.searchClients(id, nome);

            if (lista.isEmpty())
                throw new EntityNotFoundException(Mensagens.ERROR_LIST_NOT_FOUND.value());

        } catch (Exception ex) {
            status = false;
            mensagem = Mensagens.ERROR_LIST_CLIENTS.value() + ex.getMessage();
        }

        return ServiceResponse.builder()
                .status(status)
                .mensagem(mensagem)
                .dados(lista).build();
    }
}
